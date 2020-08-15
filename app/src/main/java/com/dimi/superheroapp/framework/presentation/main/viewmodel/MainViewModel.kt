package com.dimi.superheroapp.framework.presentation.main.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.business.interactors.UseCases
import com.dimi.superheroapp.framework.presentation.main.state.MainStateEvent.*
import com.dimi.superheroapp.framework.presentation.main.state.MainViewState
import com.dimi.superheroapp.framework.presentation.common.BaseViewModel
import com.dimi.superheroapp.util.GenericErrors.SHORT_QUERY_ERROR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel
@ViewModelInject
constructor(
    private val useCases: UseCases,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<MainViewState>() {


    override fun handleNewData(data: MainViewState) {
        data.let { viewState ->
            viewState.superHeroList?.let { superHeroList ->
                setMovieList(superHeroList)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<MainViewState>?> = when (stateEvent) {

            is SearchHeroes -> {
                if (stateEvent.clearLayoutManagerState) {
                    clearLayoutManagerState()
                }
                useCases.searchSuperHeroes.searchSuperHeroes(
                    query = getSearchQuery(),
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState() = MainViewState()

    fun createShortSearchQueryMessage() {
        setStateEvent(
            CreateStateMessageEvent(
                stateMessage = StateMessage(
                    response = Response(
                        SHORT_QUERY_ERROR,
                        UIComponentType.Toast(),
                        MessageType.Error()
                    )
                )
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}