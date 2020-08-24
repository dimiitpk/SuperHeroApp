package com.dimi.superheroapp.presentation.main.state

import android.os.Parcelable
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.state.*
import kotlinx.android.parcel.Parcelize

const val MAIN_VIEW_STATE_BUNDLE_KEY = "com.dimi.superheroapp.presentation.main.state.MainViewState"

@Parcelize
data class MainViewState(

    var superHeroList: ArrayList<SuperHero>? = null,
    var searchQuery: String? = null,
    var layoutManagerState: Parcelable? = null,
    var superHeroDetail: SuperHero? = null,
    var filter: String? = null,
    var order: String? = null

) : Parcelable, ViewState {

    // every "is" statement is new object checking and setting
    override fun setData(vararg any: Any?) {
        for( data in any )
            when( data ) {
                is ArrayList<*> -> {
                    val result = data.filterIsInstance<SuperHero>()
                    if( result.isNotEmpty() ) superHeroList = ArrayList( result )
                }
                else -> { }
            }
    }
}