package com.snow.diary.core.database.cross

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "dream_person_crossref",
    primaryKeys = ["dreamId", "personId"]
)
data class DreamPersonCrossref(

    @ColumnInfo(index = true)
    val dreamId: Long,

    @ColumnInfo(index = true)
    val personId: Long

)
