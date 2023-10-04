package com.snow.diary.core.io.data

import com.snow.diary.core.io.data.entity.DreamEntity
import com.snow.diary.core.io.data.entity.LocationEntity
import com.snow.diary.core.io.data.entity.PersonEntity
import com.snow.diary.core.io.data.entity.RelationEntity
import kotlinx.serialization.Serializable


@Serializable
internal data class SerializableIOData(

    val dreams: List<DreamEntity>,

    val persons: List<PersonEntity>,

    val locations: List<LocationEntity>,

    val relations: List<RelationEntity>,

    val dreamPersonCrossref: List<Crossref>,

    val dreamLocationCrossref: List<Crossref>,

    val personRelationCrossrefs: List<Crossref>

){

    constructor(data: IOData) : this (
        dreams = data.dreams.map { DreamEntity(it) },
        persons = data.persons.map { PersonEntity(it) },
        locations = data.locations.map { LocationEntity(it) },
        relations = data.relations.map { RelationEntity(it) },
        dreamPersonCrossref = data.dreamPersonCrossrefs,
        dreamLocationCrossref = data.dreamLocationsCrossrefs,
        personRelationCrossrefs = data.personRelationCrossrefs
    )

}