package com.snow.diary.database.model.cross

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.snow.diary.database.cross.DreamLocationCrossref
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.LocationEntity

data class DreamWithLocations(

    @Embedded val dream: DreamEntity,

    @Relation(
        parentColumn = "dreamId",
        entityColumn = "locationId",
        associateBy = Junction(DreamLocationCrossref::class)
    )
    val persons: List<LocationEntity>

)

data class LocationWithDreams(

    @Embedded val location: LocationEntity,

    @Relation(
        parentColumn = "locationId",
        entityColumn = "dreamId",
        associateBy = Junction(DreamLocationCrossref::class)
    )
    val dreams: List<DreamEntity>

)
