package com.dimi.superheroapp.framework.datasource.cache.database

import androidx.room.*
import com.dimi.superheroapp.framework.datasource.cache.model.SuperHeroCacheEntity

const val QUERY_ORDER_ASC : String = "ASC"
const val QUERY_ORDER_DESC : String = "DESC"
const val QUERY_FILTER_NAME = "name"
const val QUERY_FILTER_STRENGTH = "strength"

const val ORDER_BY_ASC_STRENGTH = QUERY_FILTER_STRENGTH + QUERY_ORDER_ASC
const val ORDER_BY_DESC_STRENGTH = QUERY_FILTER_STRENGTH + QUERY_ORDER_DESC
const val ORDER_BY_ASC_NAME = QUERY_FILTER_NAME + QUERY_ORDER_ASC
const val ORDER_BY_DESC_NAME = QUERY_FILTER_NAME + QUERY_ORDER_DESC

@Dao
interface MainDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(superHero: SuperHeroCacheEntity): Long

    @Update
    suspend fun update(superHero: SuperHeroCacheEntity)

    @Query("""
        SELECT * FROM superheroes 
        WHERE name LIKE '%' || :query || '%' 
        OR full_name LIKE '%' || :query || '%' 
        ORDER BY name DESC
        """)
    suspend fun getSuperHeroes(
        query: String
    ): List<SuperHeroCacheEntity>

    @Query("""
        SELECT * FROM superheroes 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY name DESC
        """)
    suspend fun searchOrderByNameDESC(
        query: String
    ): List<SuperHeroCacheEntity>

    @Query("""
        SELECT * FROM superheroes 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY name ASC 
        """)
    suspend fun searchOrderByNameASC(
        query: String
    ): List<SuperHeroCacheEntity>

    @Query("""
        SELECT * FROM superheroes 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY strength DESC
        """)
    suspend fun searchOrderByStrengthDESC(
        query: String
    ): List<SuperHeroCacheEntity>

    @Query("""
        SELECT * FROM superheroes 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY strength ASC 
        """)
    suspend fun searchOrderByStrengthASC(
        query: String
    ): List<SuperHeroCacheEntity>
}

suspend fun MainDao.searchSuperHeroes(
    query: String,
    filterAndOrder: String
): List<SuperHeroCacheEntity> {

    when {

        filterAndOrder.contains(ORDER_BY_DESC_STRENGTH) ->{
            return searchOrderByStrengthDESC(query = query)
        }

        filterAndOrder.contains(ORDER_BY_ASC_STRENGTH) ->{
            return searchOrderByStrengthASC(query = query)
        }

        filterAndOrder.contains(ORDER_BY_DESC_NAME) ->{
            return searchOrderByNameDESC(query = query)
        }

        filterAndOrder.contains(ORDER_BY_ASC_NAME) ->{
            return searchOrderByNameASC(query = query)
        }
        else ->

            return searchOrderByNameASC(
                query = query
            )
    }
}












