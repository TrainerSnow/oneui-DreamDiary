package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.cross.DreamWithLocations
import com.snow.diary.database.model.cross.DreamWithPersons
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Transaction
    @Upsert
    fun upsert(vararg entity: DreamEntity)

    @Transaction
    @Delete
    fun delete(vararg dream: DreamEntity)

    @Transaction
    @Query("SELECT * FROM dream")
    fun getAll(): Flow<List<DreamEntity>>

    @Transaction
    @Query("SELECT * FROM Dream WHERE dreamId = :id")
    fun getById(id: Long): Flow<DreamEntity?>

    @Transaction
    @Query("SELECT * FROM dream WHERE dreamId = :id")
    fun getDreamWithPersonsById(id: Long): Flow<DreamWithPersons?>

    @Transaction
    @Query("SELECT * FROM dream")
    fun getAllDreamsWithPersons(): Flow<List<DreamWithPersons>>

    @Transaction
    @Query("SELECT * FROM dream WHERE dreamId = :id")
    fun getDreamWithLocationsById(id: Long): Flow<DreamWithLocations?>

    @Transaction
    @Query("SELECT * FROM dream")
    fun getAllDreamsWithLocations(): Flow<List<DreamWithLocations>>

}