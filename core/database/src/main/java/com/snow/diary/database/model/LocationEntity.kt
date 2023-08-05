package com.snow.diary.database.model

import android.graphics.PointF
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "location"
)
data class LocationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,

    val coordinates: PointF,

    val notes: String
)
