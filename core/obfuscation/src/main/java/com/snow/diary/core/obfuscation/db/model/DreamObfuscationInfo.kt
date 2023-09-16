package com.snow.diary.core.obfuscation.db.model;

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "dream_obfuscation_info"
)
data class DreamObfuscationInfo(

    @PrimaryKey(false)
    val dreamId: Long,

    val dreamNote: String? = null,

    val dreamDescription: String

)