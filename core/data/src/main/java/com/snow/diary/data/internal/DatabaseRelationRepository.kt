package com.snow.diary.data.internal;

import com.snow.diary.common.map
import com.snow.diary.common.sortedDirectional
import com.snow.diary.data.repository.RelationRepository
import com.snow.diary.database.dao.RelationDao
import com.snow.diary.database.model.RelationEntity
import com.snow.diary.database.model.asModel
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseRelationRepository @Inject constructor(
    val relationDao: RelationDao
) : RelationRepository {
    override suspend fun upsertRelation(vararg relation: Relation) = relationDao
        .upsert(*relation.map { RelationEntity(it) })

    override suspend fun deleteRelation(vararg relation: Relation) = relationDao
        .delete(*relation.map { RelationEntity(it) })

    override fun getAllRelations(sortConfig: SortConfig): Flow<List<Relation>> = relationDao
        .getAll()
        .map { relations ->
            relations
                .sort(sortConfig)
                .map(RelationEntity::asModel)
        }

    override fun getRelationById(id: Long): Flow<Relation?> = relationDao
        .getById(id)
        .map { it?.asModel }

    override fun getRelationByPerson(person: Person): Flow<Relation> = getRelationById(
        person.relationId
    ).map {
        it!! //Assert non null because of foreign key constraint of Person#relationId
    }
}

private fun List<RelationEntity>.sort(sortConfig: SortConfig): List<RelationEntity> =
    when (sortConfig.mode) {
        SortMode.Alphabetically -> sortedBy { it.name }
        SortMode.Length -> sortedBy { it.name.length }
        else -> this
    }.sortedDirectional(sortConfig.direction)