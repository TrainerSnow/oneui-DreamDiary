package com.snow.diary.core.database.model

import android.graphics.PointF
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.core.database.IModelMappable
import com.snow.diary.core.model.data.Coordinates
import com.snow.diary.core.model.data.Location

@Entity(
    tableName = "location"
)
data class LocationEntity(

    @PrimaryKey(autoGenerate = true)
    val locationId: Long? = null,

    val name: String,

    val coordinates: PointF,

    val notes: String?
) : IModelMappable<Location> {

    override fun toModel() =
        Location(locationId, name, Coordinates(coordinates.x, coordinates.y), notes)

    constructor(location: Location) : this(
        location.id,
        location.name,
        location.coordinates.let {
            PointF(it.x, it.y)
        },
        location.notes
    )

}
