package com.snow.diary.core.obfuscation.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "person_obfuscation_info"
)
data class PersonObfuscationInfo (

    @PrimaryKey(false)
    val personId: Long,

    val personName: String,

    val personNote: String? = null

)