package com.dimi.superheroapp.business.interactors.main

class MainUseCases(
    val searchSuperHeroes: SearchSuperHeroes,
    val searchSuperHeroesFromCache: SuperHeroesFromCache
)