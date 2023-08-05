package com.snow.diary.data.repository

import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

interface RelationRepository {

    suspend fun upsertRelation(vararg relation: Relation)

    suspend fun deleteRelation(vararg relation: Relation)

    fun getAllRelations(
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<Relation>>

    fun getRelationById(id: Long): Flow<Relation?>

    fun getRelationByPerson(
        person: Person
    ): Flow<Relation>

}