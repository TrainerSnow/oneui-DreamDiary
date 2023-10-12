package com.snow.diary.core.database.cross

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.database.model.LocationEntity

@Entity(
    tableName = "dream_location_crossref",
    primaryKeys = ["dreamId", "locationId"],
    foreignKeys = [
        ForeignKey(
            entity = DreamEntity::class,
            parentColumns = ["dreamId"],
            childColumns = ["dreamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DreamLocationCrossref(

    @ColumnInfo(index = true)
    val dreamId: Long,

    @ColumnInfo(index = true)
    val locationId: Long

)
