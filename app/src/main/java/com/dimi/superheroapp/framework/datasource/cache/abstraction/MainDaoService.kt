package com.dimi.superheroapp.framework.datasource.cache.abstraction

import com.dimi.superheroapp.business.domain.model.SuperHero


interface MainDaoService {

    suspend fun insertSuperHero( superHero: SuperHero ): Long

    suspend fun updateSuperHero( superHero: SuperHero )

    suspend fun searchSuperHeroes(
        query: String,
        filterAndOrder: String
    ): List<SuperHero>
}