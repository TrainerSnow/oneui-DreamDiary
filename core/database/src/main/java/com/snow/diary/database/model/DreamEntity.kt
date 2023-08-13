package com.snow.diary.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.model.data.Dream
import java.time.LocalDate

@Entity(
    tableName = "dream"
)
data class DreamEntity(

    @PrimaryKey(autoGenerate = true)
    val dreamId: Long,

    val description: String,

    val note: String?,

    val isFavourite: Boolean,

    val created: LocalDate,

    val updated: LocalDate,

    val clearness: Float?,

    val happiness: Float?
){

    constructor(dream: Dream) : this(
        dream.id,
        dream.description,
        dream.note,
        dream.isFavourite,
        dream.created,
        dream.updated,
        dream.clearness,
        dream.happiness
    )

}

val DreamEntity.asModel: Dream
    get() = Dream(
        dreamId, description, note, isFavourite, created, updated, clearness, happiness
    )
