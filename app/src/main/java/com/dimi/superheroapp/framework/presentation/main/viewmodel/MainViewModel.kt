package com.dimi.superheroapp.framework.presentation.main.viewmodel

import android.content.SharedPreferences
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.business.interactors.UseCases
import com.dimi.superheroapp.framework.datasource.PreferenceKeys.QUERY_FILTER
import com.dimi.superheroapp.framework.datasource.PreferenceKeys.QUERY_ORDER
import com.dimi.superheroapp.framework.datasource.cache.database.QUERY_FILTER_NAME
import com.dimi.superheroapp.framework.datasource.cache.database.QUERY_ORDER_ASC
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
    private val editor: SharedPreferences.Editor,
    sharedPreferences: SharedPreferences,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<MainViewState>() {

    init {
        setFilter(
            sharedPreferences.getString( QUERY_FILTER,  QUERY_FILTER_NAME )
        )
        setOrder(
            sharedPreferences.getString( QUERY_ORDER,  QUERY_ORDER_ASC )
        )
    }

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
                    filterAndOrder = getFilter() + getOrder(),
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

    fun saveFilterOptions(filter: String, order: String){
        editor.putString(QUERY_FILTER, filter)
        editor.apply()

        editor.putString(QUERY_ORDER, order)
        editor.apply()
    }
}