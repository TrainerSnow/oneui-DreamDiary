package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.snow.diary.database.cross.DreamLocationCrossref
import com.snow.diary.database.cross.DreamPersonCrossref
import kotlinx.coroutines.flow.Flow

@Dao
interface CrossrefDao {

    @Transaction
    @Insert
    fun addDreamPersonCrossref(crossref: DreamPersonCrossref)

    @Transaction
    @Insert
    fun addDreamLocationCrossref(crossref: DreamLocationCrossref)

    @Transaction
    @Delete
    fun deleteDreamPersonCrossref(crossref: DreamPersonCrossref)

    @Transaction
    @Delete
    fun deleteDreamLocationCrossref(crossref: DreamLocationCrossref)

    @Transaction
    @Query("SELECT * FROM dream_location_crossref")
    fun getAllDreamLocationCrossrefs(): Flow<List<DreamLocationCrossref>>

    @Transaction
    @Query("SELECT * FROM dream_person_crossref")
    fun getAllDreamPersonCrossrefs(): Flow<List<DreamPersonCrossref>>

}