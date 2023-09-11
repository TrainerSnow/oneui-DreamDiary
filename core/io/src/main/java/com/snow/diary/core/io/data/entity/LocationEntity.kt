package com.snow.diary.core.io.data.entity

import com.snow.diary.core.io.data.serialization.CoordinatesSerializer
import com.snow.diary.core.model.data.Coordinates
import com.snow.diary.core.model.data.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationEntity(

    val id: Long? = null,

    val name: String,

    @Serializable(CoordinatesSerializer::class)
    val coordinates: Coordinates,

    val notes: String?

){

    fun toModel() =
        Location(id, name, coordinates, notes)

    constructor(location: Location) : this(
        location.id, location.name, location.coordinates, location.notes
    )

}