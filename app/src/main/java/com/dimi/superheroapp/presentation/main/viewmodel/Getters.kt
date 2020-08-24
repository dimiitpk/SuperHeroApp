package com.dimi.superheroapp.presentation.main.viewmodel

import android.os.Parcelable
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.framework.cache.database.QUERY_FILTER_NAME
import com.dimi.superheroapp.framework.cache.database.QUERY_ORDER_DESC
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
fun MainViewModel.getFilter(): String {
    return getCurrentViewStateOrNew().filter
        ?: QUERY_FILTER_NAME
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.getClickedSuperHero(): SuperHero? {
    return getCurrentViewStateOrNew().superHeroDetail
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().order
        ?: QUERY_ORDER_DESC
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.getLayoutManagerState(): Parcelable? {
    getCurrentViewStateOrNew().let {
        return it.layoutManagerState
    }
}