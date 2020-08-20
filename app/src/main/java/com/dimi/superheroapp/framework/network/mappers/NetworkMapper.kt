package com.dimi.superheroapp.framework.network.mappers

import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.util.EntityMapper
import com.dimi.superheroapp.framework.network.model.SuperHeroNetworkEntity
import javax.inject.Inject

class NetworkMapper
@Inject constructor() : EntityMapper<SuperHeroNetworkEntity, SuperHero> {

    fun mapFromEntityList(entities: List<SuperHeroNetworkEntity>): List<SuperHero> {
        return entities.map { mapFromEntity(it) }
    }

    override fun mapFromEntity(entity: SuperHeroNetworkEntity): SuperHero {
        return SuperHero(
            id = Integer.parseInt(entity.id),
            name = entity.name,
            aliases = entity.biography.aliases,
            alignment = entity.biography.alignment,
            alterEgos = entity.biography.alterEgos,
            firstAppearance = entity.biography.firstAppearance,
            fullName = entity.biography.fullName,
            placeOfBirth = entity.biography.placeOfBirth,
            publisher = entity.biography.publisher,
            image = entity.image.url,
            combat = if( entity.powerStats.combat == "null" ) 0 else Integer.parseInt(entity.powerStats.combat),
            speed = if( entity.powerStats.speed == "null" ) 0 else Integer.parseInt(entity.powerStats.speed),
            durability = if( entity.powerStats.durability == "null" ) 0 else Integer.parseInt(entity.powerStats.durability),
            power = if( entity.powerStats.power == "null" ) 0 else Integer.parseInt(entity.powerStats.power),
            intelligence = if( entity.powerStats.intelligence == "null" ) 0 else Integer.parseInt(entity.powerStats.intelligence),
            strength = if( entity.powerStats.strength == "null" ) 0 else Integer.parseInt(entity.powerStats.strength)
        )
    }

    override fun mapToEntity(domainModel: SuperHero): SuperHeroNetworkEntity? {
        return null
    }
}