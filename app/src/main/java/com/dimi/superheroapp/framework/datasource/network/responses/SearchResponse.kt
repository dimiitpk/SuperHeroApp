package com.dimi.superheroapp.framework.datasource.network.responses

import com.dimi.superheroapp.framework.datasource.network.model.SuperHeroNetworkEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("error")
    @Expose
    var error: String?,

    @SerializedName("results-for")
    @Expose
    var searchQuery: String,

    @SerializedName("results")
    @Expose
    var results: List<SuperHeroNetworkEntity>
)