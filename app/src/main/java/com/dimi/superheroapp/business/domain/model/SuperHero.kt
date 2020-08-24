package com.dimi.superheroapp.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.lang.StringBuilder

@Parcelize
data class SuperHero(

    var id: Int,
    var name: String,
    var fullName: String,
    var alterEgos: String,
    var aliases: List<String>,
    var placeOfBirth: String,
    var firstAppearance: String,
    var publisher: String,
    var alignment: String,
    var image: String,
    var intelligence: Int,
    var speed: Int,
    var power: Int,
    var combat: Int,
    var durability: Int,
    var strength: Int,
    var expended: Boolean = false
) : Parcelable {



    override fun toString(): String {
        return "SuperHero(id=$id, name='$name', fullName='$fullName', alterEgos='$alterEgos', aliases=$aliases, placeOfBirth='$placeOfBirth', firstAppearance='$firstAppearance', publisher='$publisher', alignment='$alignment', image='$image')"
    }

    fun getValidPlaceOfBirth() : String {
        return if( placeOfBirth.isNotBlank() && placeOfBirth != "-")
            placeOfBirth
        else "Unknown"
    }

    fun getValidPublisher() : String {
        return if( publisher.isNotBlank() && publisher != "null")
            publisher
        else "Unknown"
    }

    fun getValidFirstAppearance() : String {
        return if( firstAppearance.isNotBlank() && firstAppearance != "-")
            firstAppearance
        else "Unknown"
    }

    fun getValidAliases() : String {
        val stringBuilder = StringBuilder()
        for (alias in this.aliases) {
            if (alias.isNotBlank() && alias != "-") {
                if (stringBuilder.isNotEmpty()) stringBuilder.append("\n")
                stringBuilder.append(alias)
            }
        }
        if (stringBuilder.isEmpty()) stringBuilder.append("There are no known aliases for this superhero.")
        return stringBuilder.toString()
    }

}