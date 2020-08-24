package com.dimi.superheroapp.business.interactors.main

import android.os.Message
import com.dimi.superheroapp.business.data.cache.CacheResponseHandler
import com.dimi.superheroapp.business.data.cache.abstraction.CacheDataSource
import com.dimi.superheroapp.business.data.util.safeCacheCall
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.business.domain.state.ViewState as ViewStateInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow

class SuperHeroesFromCache(
    private val cacheDataSource: CacheDataSource
) {

    fun <ViewState : ViewStateInterface> searchSuperHeroes(
        viewState: ViewState,
        query: String,
        filterAndOrder: String,
        stateEvent: StateEvent
    ) = flow {

        val response = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.searchSuperHeroes(query, filterAndOrder)
        }

        val result = object : CacheResponseHandler<ViewState, List<SuperHero>>(
            viewState = viewState,
            response = response,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultViewState: ViewState): DataState<ViewState>? {

                return DataState.data(
                    response = Response(
                        message = SEARCH_SUPERHEROES_SUCCESSFUL,
                        messageType = MessageType.Success(),
                        uiComponentType = UIComponentType.None()
                    ),
                    data = resultViewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(result)
    }

    companion object {
        const val SEARCH_SUPERHEROES_SUCCESSFUL = "Successfully retrieved list of superheroes."
    }
}