package com.dimi.superheroapp.framework.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dimi.superheroapp.framework.presentation.main.state.MAIN_VIEW_STATE_BUNDLE_KEY
import com.dimi.superheroapp.framework.presentation.main.state.MainViewState
import com.dimi.superheroapp.framework.presentation.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.lang.ClassCastException

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes fragmentMovie: Int) : Fragment(fragmentMovie) {

    lateinit var uiController: UIController

    val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChannel()

        restoreInstanceState(savedInstanceState)
    }

    private fun setupChannel() = viewModel.setupChannel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            if (it is MainActivity) {
                try {
                    uiController = context as UIController
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[MAIN_VIEW_STATE_BUNDLE_KEY] as MainViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        viewState?.superHeroList = ArrayList()

        outState.putParcelable(
            MAIN_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }
}