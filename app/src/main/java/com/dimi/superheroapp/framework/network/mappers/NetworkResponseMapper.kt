package com.dimi.superheroapp.framework.network.mappers

import com.dimi.superheroapp.business.data.network.responses.SearchResponse
import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.util.EntityMapper
import com.dimi.superheroapp.framework.network.model.SuperHeroNetworkEntity
import com.dimi.superheroapp.framework.network.responses.NetworkSearchResponse
import javax.inject.Inject

class NetworkResponseMapper
@Inject constructor(private val networkMapper: NetworkMapper) : EntityMapper<NetworkSearchResponse, SearchResponse> {

    override fun mapFromEntity(entity: NetworkSearchResponse): SearchResponse {


        return SearchResponse(
            error = entity.error,
            response = entity.response,
            searchQuery = entity.searchQuery,
            results = entity.results?.let { networkMapper.mapFromEntityList(entities = it) } ?: ArrayList()
        )
    }

    override fun mapToEntity(domainModel: SearchResponse): NetworkSearchResponse? {
        return null
    }
}