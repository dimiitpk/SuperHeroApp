package com.dimi.superheroapp.framework.datasource.network.model

import com.dimi.superheroapp.framework.datasource.network.responses.BiographyResponse
import com.dimi.superheroapp.framework.datasource.network.responses.ImageResponse
import com.dimi.superheroapp.framework.datasource.network.responses.PowerStatsResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuperHeroNetworkEntity(

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("powerstats")
    @Expose
    var powerStats: PowerStatsResponse,

    @SerializedName("biography")
    @Expose
    var biography: BiographyResponse,

    @SerializedName("image")
    @Expose
    var image: ImageResponse
)