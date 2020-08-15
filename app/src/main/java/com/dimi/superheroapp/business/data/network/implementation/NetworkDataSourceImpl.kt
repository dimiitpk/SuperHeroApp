package com.dimi.superheroapp.business.data.network.implementation

import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.framework.datasource.network.abstraction.MainApiService
import com.dimi.superheroapp.framework.datasource.network.responses.SearchResponse
import javax.inject.Inject

class NetworkDataSourceImpl
@Inject constructor(
    private val mainApiService: MainApiService
) : NetworkDataSource {

    override suspend fun searchSuperHeroes(query: String): SearchResponse {
        return mainApiService.searchSuperHeroes(query)
    }
}