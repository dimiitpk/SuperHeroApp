package com.dimi.superheroapp.framework.presentation.main.viewmodel

import android.os.Parcelable
import com.dimi.superheroapp.business.domain.model.SuperHero
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().searchQuery
        ?: ""
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.getSuperHeroList(): ArrayList<SuperHero>? {
    return getCurrentViewStateOrNew().superHeroList
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.getLayoutManagerState(): Parcelable? {
    getCurrentViewStateOrNew().let {
        return it.layoutManagerState
    }
}