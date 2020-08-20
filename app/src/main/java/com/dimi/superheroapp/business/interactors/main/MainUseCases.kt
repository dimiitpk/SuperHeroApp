package com.dimi.superheroapp.business.interactors.main

import com.dimi.superheroapp.business.domain.state.ViewState

class MainUseCases<T : ViewState>(
    val searchSuperHeroes: SearchSuperHeroes<T>
)