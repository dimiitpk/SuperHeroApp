package com.dimi.superheroapp.framework.network.implementation

import com.dimi.superheroapp.business.data.network.abstraction.NetworkDataSource
import com.dimi.superheroapp.business.data.network.responses.SearchResponse
import com.dimi.superheroapp.framework.network.api.MainApi
import com.dimi.superheroapp.framework.network.mappers.NetworkResponseMapper

class MainApiServiceImpl(
    private val mainApi: MainApi,
    private val networkResponseMapper: NetworkResponseMapper
) : NetworkDataSource {

    override suspend fun searchSuperHeroes(
        query: String
    ): SearchResponse {
        return networkResponseMapper.mapFromEntity(mainApi.searchSuperHeroes(query))
    }
}