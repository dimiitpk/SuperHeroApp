package com.dimi.superheroapp.framework.network.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PowerStatsResponse(

    @SerializedName("intelligence")
    @Expose
    var intelligence: String,

    @SerializedName("strength")
    @Expose
    var strength: String,

    @SerializedName("speed")
    @Expose
    var speed: String,

    @SerializedName("durability")
    @Expose
    var durability: String,

    @SerializedName("power")
    @Expose
    var power: String,

    @SerializedName("combat")
    @Expose
    var combat: String
)