package com.snow.diary.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "dream"
)
data class DreamEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val description: String,

    val note: String?,

    val created: Date,

    val updated: Date,

    val clearness: Float?,

    val happiness: Float?
)
