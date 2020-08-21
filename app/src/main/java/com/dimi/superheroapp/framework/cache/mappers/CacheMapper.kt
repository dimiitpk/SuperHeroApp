package com.dimi.superheroapp.framework.cache.mappers

import com.dimi.superheroapp.business.domain.model.SuperHero
import com.dimi.superheroapp.business.domain.util.EntityMapper
import com.dimi.superheroapp.framework.cache.model.SuperHeroCacheEntity
import com.dimi.superheroapp.util.ConvertUtils
import javax.inject.Inject

class CacheMapper
@Inject
constructor() : EntityMapper<SuperHeroCacheEntity, SuperHero> {

    override fun mapFromEntity(entity: SuperHeroCacheEntity): SuperHero {
        return SuperHero(
            id = entity.id,
            strength = entity.strength,
            intelligence = entity.intelligence,
            power = entity.power,
            durability = entity.durability,
            speed = entity.speed,
            combat = entity.combat,
            image = entity.image,
            name = entity.name,
            publisher = entity.publisher,
            placeOfBirth = entity.placeOfBirth,
            fullName = entity.fullName,
            firstAppearance = entity.firstAppearance,
            alterEgos = entity.alterEgos,
            alignment = entity.alignment,
            aliases = ConvertUtils.fromStringToStringArray(entity.aliases)
        )
    }

    override fun mapToEntity(domainModel: SuperHero): SuperHeroCacheEntity {
        return SuperHeroCacheEntity(
            id = domainModel.id,
            strength = domainModel.strength,
            intelligence = domainModel.intelligence,
            power = domainModel.power,
            durability = domainModel.durability,
            speed = domainModel.speed,
            combat = domainModel.combat,
            image = domainModel.image,
            name = domainModel.name,
            publisher = domainModel.publisher,
            placeOfBirth = domainModel.placeOfBirth,
            fullName = domainModel.fullName,
            firstAppearance = domainModel.firstAppearance,
            alterEgos = domainModel.alterEgos,
            alignment = domainModel.alignment,
            aliases = ConvertUtils.fromStringArrayToString(ArrayList(domainModel.aliases))
        )
    }


    fun mapFromEntityList(entities: List<SuperHeroCacheEntity>): List<SuperHero> {
        return entities.map { mapFromEntity(it) }
    }

}







