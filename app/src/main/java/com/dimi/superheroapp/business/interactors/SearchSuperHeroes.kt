package com.dimi.superheroapp.business.interactors

import com.dimi.superheroapp.business.data.network.ApiResponseHandler
import com.dimi.superheroapp.business.data.network.NetworkErrors
import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.business.data.util.safeApiCall
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.*
import com.dimi.superheroapp.framework.datasource.network.mappers.NetworkMapper
import com.dimi.superheroapp.framework.datasource.network.responses.SearchResponse
import com.dimi.superheroapp.framework.presentation.main.state.MainViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchSuperHeroes
@Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val networkMapper: NetworkMapper
) {
    fun searchSuperHeroes(
        query: String,
        stateEvent: StateEvent
    ) = flow {

        val response = safeApiCall(Dispatchers.IO) {
            networkDataSource.searchSuperHeroes(query = query)
        }

        val apiResponse = object : ApiResponseHandler<MainViewState, SearchResponse>(
            response = response,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: SearchResponse): DataState<MainViewState>? {

                var message = SEARCH_SUPERHEROES_SUCCESSFUL
                var componentType: UIComponentType = UIComponentType.None()
                var heroList: ArrayList<SuperHero> = ArrayList()

                if (resultObj.error.isNullOrEmpty()) {
                    if (resultObj.results.isEmpty()) {
                        message = SEARCH_SUPERHEROES_FAILED
                        componentType = UIComponentType.Toast()
                    } else heroList = ArrayList(networkMapper.mapFromEntityList(resultObj.results))
                } else {
                    if (resultObj.error == NetworkErrors.NO_MATCHING_RESULTS_ERROR) {
                        message = "$SEARCH_NO_MATCHING_RESULTS $query"
                        componentType = UIComponentType.Toast()
                    }
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = componentType,
                        messageType = MessageType.Success()
                    ),
                    data = MainViewState(superHeroList = heroList),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(apiResponse)
    }

    companion object {
        const val SEARCH_SUPERHEROES_SUCCESSFUL = "Successfully retrieved list of superheroes."
        const val SEARCH_SUPERHEROES_FAILED = "Failed to retrieve the list of superheroes."
        const val SEARCH_NO_MATCHING_RESULTS = "Failed to retrieve results for:"
    }
}