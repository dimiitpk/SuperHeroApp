package com.dimi.superheroapp.framework.datasource.network.api

import com.dimi.superheroapp.framework.datasource.network.responses.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {

    @GET("search/{query}")
    suspend fun searchSuperHeroes(
        @Path("query") query: String
    ) : SearchResponse

}