package com.dimi.superheroapp.presentation.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.RequestManager
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.StateMessageCallback
import com.dimi.superheroapp.business.interactors.main.SearchSuperHeroes.Companion.SEARCH_SUPERHEROES_SUCCESSFUL
import com.dimi.superheroapp.framework.cache.database.QUERY_FILTER_NAME
import com.dimi.superheroapp.framework.cache.database.QUERY_FILTER_STRENGTH
import com.dimi.superheroapp.framework.cache.database.QUERY_ORDER_ASC
import com.dimi.superheroapp.framework.cache.database.QUERY_ORDER_DESC
import com.dimi.superheroapp.presentation.common.fadeIn
import com.dimi.superheroapp.presentation.common.fadeOut
import com.dimi.superheroapp.presentation.common.gone
import com.dimi.superheroapp.presentation.common.visible
import com.dimi.superheroapp.presentation.main.viewmodel.*
import com.dimi.superheroapp.presentation.common.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_filter.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class SearchFragment
constructor(
    private val requestManager: RequestManager
) : BaseFragment(R.layout.fragment_search), SwipeRefreshLayout.OnRefreshListener,
    SuperHeroListAdapter.Interaction {

    private lateinit var searchView: SearchView
    private lateinit var recyclerAdapter: SuperHeroListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_highlight)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()

        pop_up_top_button.setOnClickListener {
            resetUI()
            uiController.expandAppBar()
        }
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->

            if (viewState != null) {
                recyclerAdapter.apply {
                    viewState.superHeroList?.let {
                        preloadGlideImages(
                            requestManager = requestManager,
                            list = it
                        )
                    }
                    submitList(
                        superHeroList = viewState.superHeroList
                    )
                }
                viewState.superHeroList?.let { superHeroList ->
                    if (superHeroList.isEmpty()) {
                        info_text_container.visible()
                        swipe_refresh.gone()
                    } else {
                        info_text_container.gone()
                        swipe_refresh.visible()
                    }
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, { stateMessage ->
            stateMessage?.let { message ->

                if (message.response.message == SEARCH_SUPERHEROES_SUCCESSFUL) {
                    viewModel.clearStateMessage()
                } else {
                    uiController.onResponseReceived(
                        response = message.response,
                        stateMessageCallback = object : StateMessageCallback {
                            override fun removeMessageFromStack() {
                                viewModel.clearStateMessage()
                            }
                        }
                    )
                }
            }
        })

    }

    private fun initRecyclerView() {

        recycler_view.apply {

            layoutManager =
                LinearLayoutManager(this@SearchFragment.context)
            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                SuperHeroListAdapter(
                    requestManager,
                    this@SearchFragment
                )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    if (firstPosition > 2) {
                        pop_up_top_button.fadeIn()
                    } else {
                        pop_up_top_button.fadeOut()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: SuperHero) {
        viewModel.setClickedSuperHero(item)
        if (!uiController.isDeviceTabletInLandscapeMode())
            findNavController().navigate(R.id.action_searchFragment_to_superHeroDetailsFragment)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                uiController.changeThemeMode()
                true
            }
            R.id.action_filter_settings -> {
                showFilterDialog()
                true
            }
            R.id.action_send -> {
                if (uiController.isDeviceTabletInLandscapeMode())
                    sendSuperHeroAsMessage()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun sendSuperHeroAsMessage() {
        val superHero = viewModel.getClickedSuperHero()
        superHero?.let {
            uiController.sendSuperHeroAsMessage(superHero)
        } ?: viewModel.createSuperHeroIsNotSelectedMessage()
    }

    private fun initSearchView(menu: Menu) {
        activity?.apply {
            val searchManager: SearchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView.apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                maxWidth = Integer.MAX_VALUE
                queryHint = "Search"
                setIconifiedByDefault(false)

                imeOptions = EditorInfo.IME_ACTION_SEARCH
                isSubmitButtonEnabled = true
            }
        }

        val searchPlate = searchView.findViewById(R.id.search_src_text) as EditText
        if (!viewModel.getSearchQuery().isBlank()) searchPlate.setText(viewModel.getSearchQuery())
        searchPlate.setOnEditorActionListener { v, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                val searchQuery = v.text.trim().toString()
                handleSearchConfirmed(searchQuery)
            }
            true
        }

        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.trim().toString()
            handleSearchConfirmed(searchQuery)
        }
    }

    private fun handleSearchConfirmed(searchQuery: String) {

        if (searchQuery.length < 2) {
            viewModel.createShortSearchQueryMessage()
        } else {
            viewModel.setQuery(searchQuery)
            onSearchQuery()
        }
    }

    private fun showFilterDialog() {

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.layout_filter)

            val view = dialog.getCustomView()

            val filter = viewModel.getFilter()
            val order = viewModel.getOrder()

            view.filter_group.apply {
                when (filter) {
                    QUERY_FILTER_STRENGTH -> check(R.id.filter_strength)
                    QUERY_FILTER_NAME -> check(R.id.filter_name)
                }
            }

            view.order_group.apply {
                when (order) {
                    QUERY_ORDER_ASC -> check(R.id.filter_asc)
                    QUERY_ORDER_DESC -> check(R.id.filter_desc)
                }
            }

            view.positive_button.setOnClickListener {
                val newFilter =
                    when (view.filter_group.checkedRadioButtonId) {
                        R.id.filter_name -> QUERY_FILTER_NAME
                        R.id.filter_strength -> QUERY_FILTER_STRENGTH
                        else -> QUERY_FILTER_NAME
                    }

                val newOrder =
                    when (view.order_group.checkedRadioButtonId) {
                        R.id.filter_desc -> QUERY_ORDER_DESC
                        else -> QUERY_ORDER_ASC
                    }

                viewModel.apply {
                    saveFilterOptions(newFilter, newOrder)
                    setFilter(newFilter)
                    setOrder(newOrder)
                }

                searchQueryFromCache(filter = true)

                dialog.dismiss()
            }

            view.negative_button.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        searchQueryFromCache()
    }

    override fun onPause() {
        super.onPause()
        saveListPosition()
    }

    override fun saveListPosition() {
        recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    private fun searchQueryFromCache(filter: Boolean = false) {
        viewModel.searchSuperHeroesInCache(filter)
        if (filter) resetUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler_view.adapter = null
    }

    override fun onRefresh() {
        onSearchQuery()
        swipe_refresh.isRefreshing = false
    }

    private fun onSearchQuery() {
        viewModel.searchSuperHeroes().let {
            resetUI()
        }
    }

    private fun resetUI() {
        recycler_view.smoothScrollToPosition(0)
        uiController.hideSoftKeyboard()
        focusable_view.requestFocus()
    }
}