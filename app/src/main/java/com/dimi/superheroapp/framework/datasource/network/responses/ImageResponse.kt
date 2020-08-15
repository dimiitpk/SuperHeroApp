package com.dimi.superheroapp.framework.datasource.network.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageResponse(

    @SerializedName("url")
    @Expose
    var url: String
)