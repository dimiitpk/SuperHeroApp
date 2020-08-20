package com.dimi.superheroapp.business.data.network.implementation

import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.business.data.network.responses.SearchResponse

class NetworkDataSourceImpl(
    private val mainApiService: NetworkDataSource
) : NetworkDataSource {

    override suspend fun searchSuperHeroes(
        query: String
    ): SearchResponse {
        return mainApiService.searchSuperHeroes(query)
    }
}