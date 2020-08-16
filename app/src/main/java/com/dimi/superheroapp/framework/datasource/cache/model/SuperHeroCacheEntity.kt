package com.dimi.superheroapp.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "superheroes")
data class SuperHeroCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "full_name")
    var fullName: String,

    @ColumnInfo(name = "alter_egos")
    var alterEgos: String,

    @ColumnInfo(name = "aliases")
    var aliases: String,

    @ColumnInfo(name = "place_of_birth")
    var placeOfBirth: String,

    @ColumnInfo(name = "first_appearance")
    var firstAppearance: String,

    @ColumnInfo(name = "publisher")
    var publisher: String,

    @ColumnInfo(name = "alignment")
    var alignment: String,

    @ColumnInfo(name = "intelligence")
    var intelligence: Int,

    @ColumnInfo(name = "speed")
    var speed: Int,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "power")
    var power: Int,

    @ColumnInfo(name = "combat")
    var combat: Int,

    @ColumnInfo(name = "durability")
    var durability: Int,

    @ColumnInfo(name = "strength")
    var strength: Int
)



