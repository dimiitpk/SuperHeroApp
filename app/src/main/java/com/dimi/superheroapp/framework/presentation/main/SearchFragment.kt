package com.dimi.superheroapp.framework.presentation.main

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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import com.dimi.superheroapp.R
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.StateMessageCallback
import com.dimi.superheroapp.business.interactors.SearchSuperHeroes.Companion.SEARCH_SUPERHEROES_SUCCESSFUL
import com.dimi.superheroapp.framework.presentation.common.gone
import com.dimi.superheroapp.framework.presentation.common.visible
import com.dimi.superheroapp.framework.presentation.main.state.MainStateEvent
import com.dimi.superheroapp.framework.presentation.main.viewmodel.*
import com.dimi.superheroapp.util.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*
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
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

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
                    if( superHeroList.isEmpty() ) {
                        info_text_container.visible()
                        swipe_refresh.gone()
                    }
                    else {
                        info_text_container.gone()
                        swipe_refresh.visible()
                    }
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer {
            uiController.displayProgressBar(it)
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { message ->

                if(message.response.message == SEARCH_SUPERHEROES_SUCCESSFUL) {
                    viewModel.clearStateMessage()
                }
                else {
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

            layoutManager = LinearLayoutManager( context, RecyclerView.VERTICAL, false)
            val spaceDecoration = SpacesItemDecoration(15)
            removeItemDecoration(spaceDecoration)
            addItemDecoration(spaceDecoration)
            recyclerAdapter =
                SuperHeroListAdapter(
                    requestManager,
                    this@SearchFragment
                )
            adapter = recyclerAdapter
        }
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
        viewModel.setStateEvent(MainStateEvent.SearchHeroes()).let {
            resetUI()
        }
    }

    private fun resetUI() {
        recycler_view.smoothScrollToPosition(0)
        uiController.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    override fun onItemSelected(position: Int, item: SuperHero) {
        viewModel.setClickedSuperHero( item )
        findNavController().navigate(R.id.action_searchFragment_to_superHeroDetailsFragment)
    }

    override fun onResume() {
        super.onResume()
        if( viewModel.getSearchQuery().isNotBlank() && viewModel.getSuperHeroList().isNullOrEmpty()) {
            viewModel.setStateEvent(MainStateEvent.SearchHeroes(clearLayoutManagerState = false))
        }
    }

    override fun onPause() {
        super.onPause()
        saveLayoutManagerState()
    }

    private fun saveLayoutManagerState() {
        recycler_view.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            recycler_view?.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        initSearchView(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            uiController.changeThemeMode()
        }
        return super.onOptionsItemSelected(item)
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
                val searchQuery = v.text.toString()
                handleSearchConfirmed(searchQuery)
            }
            true
        }

        val searchButton = searchView.findViewById(R.id.search_go_btn) as View
        searchButton.setOnClickListener {
            val searchQuery = searchPlate.text.toString()
            handleSearchConfirmed(searchQuery)
        }
    }

    private fun handleSearchConfirmed(searchQuery: String) {

        if( searchQuery.length < 2 ) {
            viewModel.createShortSearchQueryMessage()
        }
        else {
            viewModel.setQuery(searchQuery)
            onSearchQuery()
        }
    }
}