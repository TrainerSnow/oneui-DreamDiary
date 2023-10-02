package com.snow.diary.core.obfuscation.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_obfuscation_info"
)
data class LocationObfuscationInfo (

    @PrimaryKey(false)
    val locationId: Long,

    val locationName: String,

    val locationNote: String? = null

)