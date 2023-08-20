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
    val locationId: Long? = null,

    val name: String,

    val coordinates: PointF,

    val notes: String
){

    constructor(location: Location) : this(
        location.id,
        location.name,
        location.coordinates,
        location.notes
    )

}

val LocationEntity.asModel: Location
    get() = Location(locationId, name, coordinates, notes)
