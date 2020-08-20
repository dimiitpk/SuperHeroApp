package com.dimi.superheroapp.framework.network.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BiographyResponse(

    @SerializedName("full-name")
    @Expose
    var fullName: String,

    @SerializedName("alter-egos")
    @Expose
    var alterEgos: String,

    @SerializedName("aliases")
    @Expose
    var aliases: List<String>,

    @SerializedName("place-of-birth")
    @Expose
    var placeOfBirth: String,

    @SerializedName("first-appearance")
    @Expose
    var firstAppearance: String,

    @SerializedName("publisher")
    @Expose
    var publisher: String,

    @SerializedName("alignment")
    @Expose
    var alignment: String
)