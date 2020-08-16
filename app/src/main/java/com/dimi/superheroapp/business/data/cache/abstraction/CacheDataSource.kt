package com.dimi.superheroapp.business.data.cache.abstraction

import com.dimi.superheroapp.business.domain.model.SuperHero


interface CacheDataSource {

    suspend fun insertSuperHero(superHero: SuperHero): Long

    suspend fun updateSuperHero(superHero: SuperHero)

    suspend fun searchSuperHeroes(
        query: String,
        filterAndOrder: String
    ): List<SuperHero>
}