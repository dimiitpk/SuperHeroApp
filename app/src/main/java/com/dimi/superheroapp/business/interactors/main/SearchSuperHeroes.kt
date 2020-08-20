package com.dimi.superheroapp.business.interactors.main

import android.util.Log
import com.dimi.superheroapp.business.data.cache.abstraction.CacheDataSource
import com.dimi.superheroapp.business.data.network.NetworkErrors
import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.business.data.network.responses.SearchResponse
import com.dimi.superheroapp.business.data.util.NetworkBoundResource
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchSuperHeroes<T : ViewState>(
    private val networkDataSource: NetworkDataSource,
    private val cacheDataSource: CacheDataSource
) {
    @FlowPreview
    fun searchSuperHeroes(
        viewState: T,
        query: String,
        filterAndOrder: String,
        stateEvent: StateEvent
    ) = object : NetworkBoundResource<SearchResponse, List<SuperHero>, T>(
        stateEvent = stateEvent,
        dispatcher = IO,
        apiCall = {
            networkDataSource.searchSuperHeroes(query)
        },
        cacheCall = {
            cacheDataSource.searchSuperHeroes(query, filterAndOrder)
        }
    ) {
        override suspend fun updateCache(networkObject: SearchResponse): Response? {

            var message =
                SEARCH_SUPERHEROES_SUCCESSFUL
            var componentType: UIComponentType = UIComponentType.None()
            var messageType: MessageType = MessageType.Success()
            val heroList: ArrayList<SuperHero>

            if (networkObject.error.isNullOrEmpty()) {
                if (networkObject.results.isEmpty()) {
                    message =
                        SEARCH_SUPERHEROES_FAILED
                    componentType = UIComponentType.Toast()
                    messageType = MessageType.Error()
                } else {
                    heroList = ArrayList(networkObject.results)
                    withContext(IO) {
                        for (hero in heroList) {
                            try {
                                launch {
                                    cacheDataSource.insertSuperHero(hero)
                                    Log.d(
                                        "SearchSuperHeroes",
                                        "updateLocalDb: inserting hero: $hero"
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    "SearchSuperHeroes",
                                    "updateLocalDb: error updating cache data on hero with name: ${hero.name}. " + "${e.message}"
                                )
                            }
                        }
                    }
                }
            } else {
                if (networkObject.error == NetworkErrors.NO_MATCHING_RESULTS_ERROR) {
                    message = "$SEARCH_NO_MATCHING_RESULTS $query"
                    componentType = UIComponentType.Toast()
                    messageType = MessageType.Error()
                }
            }
            return Response(
                message = message,
                uiComponentType = componentType,
                messageType = messageType
            )
        }

        override fun handleCacheSuccess(
            resultObj: List<SuperHero>,
            cacheUpdateResponse: Response?,
            stateEvent: StateEvent?
        ): DataState<T> {

            viewState.setData(ArrayList(resultObj))

            return DataState.data(
                response = cacheUpdateResponse
                    ?: Response(
                        message = SEARCH_SUPERHEROES_SUCCESSFUL,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                data = viewState,
                stateEvent = stateEvent
            )
        }
    }.result

    companion object {
        const val SEARCH_SUPERHEROES_SUCCESSFUL = "Successfully retrieved list of superheroes."
        const val SEARCH_SUPERHEROES_FAILED = "Failed to retrieve the list of superheroes."
        const val SEARCH_NO_MATCHING_RESULTS = "Failed to retrieve results for:"
    }
}
