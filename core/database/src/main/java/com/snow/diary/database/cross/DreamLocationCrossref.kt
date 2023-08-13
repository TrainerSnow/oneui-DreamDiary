package com.snow.diary.database.cross

import androidx.room.Entity

@Entity(
    tableName = "dream_location_crossref",
    primaryKeys = ["dreamId", "locationId"]
)
data class DreamLocationCrossref(

    val dreamId: Long,

    val locationId: Long

)
