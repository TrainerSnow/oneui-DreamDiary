package com.snow.diary.database.cross

import androidx.room.Entity

@Entity(
    tableName = "dream_location_crossref"
)
data class DreamLocationCrossref(

    val dreamId: Long,

    val locationId: Long

)
