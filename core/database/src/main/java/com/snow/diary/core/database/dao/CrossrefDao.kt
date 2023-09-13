package com.snow.diary.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.snow.diary.core.database.cross.DreamLocationCrossref
import com.snow.diary.core.database.cross.DreamPersonCrossref
import com.snow.diary.core.database.cross.PersonRelationCrossref
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
    @Insert
    fun addPersonRelationCrossref(crossref: PersonRelationCrossref)

    @Transaction
    @Delete
    fun deleteDreamPersonCrossref(crossref: DreamPersonCrossref)

    @Transaction
    @Delete
    fun deleteDreamLocationCrossref(crossref: DreamLocationCrossref)

    @Transaction
    @Delete
    fun deletePersonRelationCrossref(crossref: PersonRelationCrossref)

    @Transaction
    @Query("SELECT * FROM dream_location_crossref")
    fun getAllDreamLocationCrossrefs(): Flow<List<DreamLocationCrossref>>

    @Transaction
    @Query("SELECT * FROM dream_person_crossref")
    fun getAllDreamPersonCrossrefs(): Flow<List<DreamPersonCrossref>>

    @Transaction
    @Query("SELECT * FROM person_relation_crossref")
    fun getAllPersonRelationCrossrefs(): Flow<List<PersonRelationCrossref>>

}