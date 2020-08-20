package com.dimi.superheroapp.business.data.network.responses

import com.dimi.superheroapp.business.domain.model.SuperHero

data class SearchResponse(

    var response: String,
    var error: String?,
    var searchQuery: String?,
    var results: List<SuperHero>
)