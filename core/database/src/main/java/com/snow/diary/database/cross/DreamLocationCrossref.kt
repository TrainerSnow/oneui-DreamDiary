package com.snow.diary.database.cross

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "dream_location_crossref",
    primaryKeys = ["dreamId", "locationId"]
)
data class DreamLocationCrossref(

    @ColumnInfo(index = true)
    val dreamId: Long,

    @ColumnInfo(index = true)
    val locationId: Long

)
