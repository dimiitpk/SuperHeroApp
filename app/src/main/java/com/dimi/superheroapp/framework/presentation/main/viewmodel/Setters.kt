package com.dimi.superheroapp.framework.presentation.main.viewmodel

import android.os.Parcelable
import com.dimi.superheroapp.business.domain.model.SuperHero
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.setMovieList( list: ArrayList<SuperHero>) {
    val update = getCurrentViewStateOrNew()
    update.superHeroList = list
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.searchQuery = query
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.setClickedSuperHero( superHero: SuperHero) {
    val update = getCurrentViewStateOrNew()
    update.superHeroDetail = superHero
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.setLayoutManagerState(layoutManagerState: Parcelable){
    val update = getCurrentViewStateOrNew()
    update.layoutManagerState = layoutManagerState
    setViewState(update)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun MainViewModel.clearLayoutManagerState(){
    val update = getCurrentViewStateOrNew()
    update.layoutManagerState = null
    setViewState(update)
}

