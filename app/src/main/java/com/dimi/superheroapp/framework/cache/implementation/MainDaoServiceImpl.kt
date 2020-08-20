package com.dimi.superheroapp.framework.cache.implementation

import com.dimi.superheroapp.business.data.cache.abstraction.CacheDataSource
import com.dimi.superheroapp.framework.cache.database.MainDao
import com.dimi.superheroapp.framework.cache.mappers.CacheMapper
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.framework.cache.database.searchSuperHeroes

class MainDaoServiceImpl(
    private val mainDao: MainDao,
    private val cacheMapper: CacheMapper
) : CacheDataSource {

    override suspend fun insertSuperHero(superHero: SuperHero): Long {
        return mainDao.insert(superHero = cacheMapper.mapToEntity(superHero))
    }

    override suspend fun updateSuperHero(superHero: SuperHero) {
        mainDao.update(cacheMapper.mapToEntity(superHero))
    }

    override suspend fun searchSuperHeroes(
        query: String,
        filterAndOrder: String
    ): List<SuperHero> {
        return cacheMapper.mapFromEntityList(mainDao.searchSuperHeroes(query, filterAndOrder))
    }
}