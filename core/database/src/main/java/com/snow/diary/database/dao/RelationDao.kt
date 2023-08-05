package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.snow.diary.database.model.RelationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationDao {
    @Transaction
    @Upsert
    fun upsert(vararg relation: RelationEntity)

    @Transaction
    @Delete
    fun delete(vararg relation: RelationEntity)

    @Transaction
    @Query("SELECT * FROM person_relation")
    fun getAllRelations(): Flow<List<RelationEntity>>

    @Transaction
    @Query("SELECT * FROM person_relation WHERE id = :id")
    fun getRelationById(id: Long): Flow<RelationEntity?>
}