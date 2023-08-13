package com.snow.diary.database.cross

import androidx.room.Entity

@Entity(
    tableName = "dream_person_crossref",
    primaryKeys = ["dreamId", "personId"]
)
data class DreamPersonCrossref(

    val dreamId: Long,

    val personId: Long

)
