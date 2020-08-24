package com.dimi.superheroapp.network.mappers

import com.dimi.superheroapp.network.responses.SearchResponse
import com.dimi.superheroapp.util.EntityMapper
import com.dimi.superheroapp.network.responses.NetworkSearchResponse
import javax.inject.Inject

class NetworkResponseMapper
@Inject constructor(private val networkMapper: NetworkMapper) :
    EntityMapper<NetworkSearchResponse, SearchResponse> {

    override fun mapFromEntity(entity: NetworkSearchResponse): SearchResponse {
        return SearchResponse(
            error = entity.error,
            response = entity.response,
            searchQuery = entity.searchQuery,
            results = entity.results?.let { networkMapper.mapFromEntityList(entities = it) }
                ?: ArrayList()
        )
    }

    override fun mapToEntity(domainModel: SearchResponse): NetworkSearchResponse? {
        return null
    }
}