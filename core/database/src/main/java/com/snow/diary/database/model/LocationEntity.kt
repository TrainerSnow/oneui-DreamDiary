package com.snow.diary.database.model

import android.graphics.PointF
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.model.data.Location

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

val LocationEntity.asModel: Location
    get() = Location(id, name, coordinates, notes)
