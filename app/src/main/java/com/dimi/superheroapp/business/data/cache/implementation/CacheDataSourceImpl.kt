package com.dimi.superheroapp.business.data.cache.implementation

import com.dimi.superheroapp.business.data.cache.abstraction.CacheDataSource
import com.dimi.superheroapp.business.domain.model.SuperHero

class CacheDataSourceImpl(
    private val mainDaoService: CacheDataSource
) : CacheDataSource {
    override suspend fun insertSuperHero(superHero: SuperHero): Long {
        return mainDaoService.insertSuperHero(superHero)
    }

    override suspend fun updateSuperHero(superHero: SuperHero) {
        mainDaoService.updateSuperHero(superHero)
    }

    override suspend fun searchSuperHeroes(
        query: String,
        filterAndOrder: String
    ): List<SuperHero> {
        return mainDaoService.searchSuperHeroes(query, filterAndOrder)
    }
}