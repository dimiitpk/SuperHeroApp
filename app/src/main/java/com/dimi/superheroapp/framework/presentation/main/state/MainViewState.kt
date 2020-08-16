package com.dimi.superheroapp.framework.presentation.main.state

import android.os.Parcelable
import com.dimi.superheroapp.business.domain.model.SuperHero
import kotlinx.android.parcel.Parcelize

const val MAIN_VIEW_STATE_BUNDLE_KEY = "com.dimi.superheroapp.framework.presentation.main.state.MainViewState"

@Parcelize
data class MainViewState (

    var superHeroList: ArrayList<SuperHero>? = null,
    var searchQuery: String? = null,
    var layoutManagerState: Parcelable? = null,
    var superHeroDetail: SuperHero? = null,
    var filter: String? = null,
    var order: String? = null

): Parcelable