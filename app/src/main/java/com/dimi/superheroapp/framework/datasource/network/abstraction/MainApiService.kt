package com.dimi.superheroapp.framework.datasource.network.abstraction

import com.dimi.superheroapp.framework.datasource.network.responses.SearchResponse

interface MainApiService {

    suspend fun searchSuperHeroes(
        query: String
    ): SearchResponse
}