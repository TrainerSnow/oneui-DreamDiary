package com.snow.diary.domain.pure

import com.snow.diary.IModelMappable
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.database.model.PersonEntity
import com.snow.diary.database.model.RelationEntity
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@JvmName("mapListToModel")
fun <Entity> List<IModelMappable<Entity>>.mapToModel() = map { it.toModel() }

@JvmName("mapFlowListToModel")
fun <Entity> Flow<List<IModelMappable<Entity>>>.mapToModel() = map { it.mapToModel() }

@JvmName("mapFlowToModel")
fun <Entity> Flow<IModelMappable<Entity>>.mapToModel() = map { it.toModel() }


@JvmName("mapListToEntityDream")
fun List<Dream>.mapToEntity() = map { DreamEntity(it) }

@JvmName("mapListToEntityPerson")
fun List<Person>.mapToEntity() = map { PersonEntity(it) }

@JvmName("mapListToEntityRelation")
fun List<Relation>.mapToEntity() = map { RelationEntity(it) }

@JvmName("mapListToEntityLocation")
fun List<Location>.mapToEntity() = map { LocationEntity(it) }