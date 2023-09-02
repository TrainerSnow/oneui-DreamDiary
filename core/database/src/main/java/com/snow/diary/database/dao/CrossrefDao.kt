package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import com.snow.diary.database.cross.DreamLocationCrossref
import com.snow.diary.database.cross.DreamPersonCrossref

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

}