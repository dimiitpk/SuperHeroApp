package com.dimi.superheroapp.framework.datasource.network.implementation


import com.dimi.superheroapp.framework.datasource.network.abstraction.MainApiService
import com.dimi.superheroapp.framework.datasource.network.api.MainApi
import com.dimi.superheroapp.framework.datasource.network.responses.SearchResponse

class MainApiServiceImpl(
    private val mainApi: MainApi
) : MainApiService {

    override suspend fun searchSuperHeroes(
        query: String
    ): SearchResponse {
        return mainApi.searchSuperHeroes(query)
    }
}