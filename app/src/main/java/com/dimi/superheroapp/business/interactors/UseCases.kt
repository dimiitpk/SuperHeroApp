package com.dimi.superheroapp.business.interactors

import javax.inject.Inject

class UseCases @Inject constructor(
    val searchSuperHeroes: SearchSuperHeroes
)