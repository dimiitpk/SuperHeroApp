package com.dimi.superheroapp.business.data.network.abstraction

import com.dimi.superheroapp.framework.datasource.network.responses.SearchResponse

interface NetworkDataSource {
     suspend fun searchSuperHeroes(
        query: String
    ): SearchResponse
}