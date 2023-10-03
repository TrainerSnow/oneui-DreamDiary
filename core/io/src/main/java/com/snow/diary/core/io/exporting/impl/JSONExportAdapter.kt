package com.snow.diary.core.io.exporting.impl

import com.snow.diary.core.io.data.Crossref
import com.snow.diary.core.io.data.IOData
import com.snow.diary.core.io.data.entity.DreamEntity
import com.snow.diary.core.io.data.entity.LocationEntity
import com.snow.diary.core.io.data.entity.PersonEntity
import com.snow.diary.core.io.data.entity.RelationEntity
import com.snow.diary.core.io.exporting.IExportAdapter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStream

class JSONExportAdapter : IExportAdapter {
    override fun export(data: IOData, os: OutputStream) {
        val jsonData = JSONIOData(data)

        val json = Json.encodeToString(jsonData)
        os.write(json.toByteArray())
    }

}


@Serializable
internal data class JSONIOData(

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