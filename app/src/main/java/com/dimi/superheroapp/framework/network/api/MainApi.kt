package com.dimi.superheroapp.framework.network.api

import com.dimi.superheroapp.framework.network.responses.NetworkSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {

    @GET("search/{query}")
    suspend fun searchSuperHeroes(
        @Path("query") query: String
    ) : NetworkSearchResponse

}