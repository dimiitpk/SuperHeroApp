package com.dimi.superheroapp.framework.datasource.cache.implementation

import com.dimi.superheroapp.framework.datasource.cache.database.MainDao
import com.dimi.superheroapp.framework.datasource.cache.mappers.CacheMapper
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.framework.datasource.cache.abstraction.MainDaoService
import com.dimi.superheroapp.framework.datasource.cache.database.searchSuperHeroes

class MainDaoServiceImpl(
    private val mainDao: MainDao,
    private val cacheMapper: CacheMapper
) : MainDaoService {

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