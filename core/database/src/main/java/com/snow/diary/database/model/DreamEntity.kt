package com.snow.diary.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.model.data.Dream
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
){

    constructor(dream: Dream) : this(
        dream.id,
        dream.description,
        dream.note,
        dream.created,
        dream.updated,
        dream.clearness,
        dream.happiness
    )

}

val DreamEntity.asModel: Dream
    get() = Dream(
        id, description, note, created, updated, clearness, happiness
    )
