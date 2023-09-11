package com.snow.diary.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.core.database.IModelMappable
import com.snow.diary.core.model.data.Dream
import java.time.LocalDate

@Entity(
    tableName = "dream"
)
data class DreamEntity(

    @PrimaryKey(autoGenerate = true)
    val dreamId: Long? = null,

    val description: String,

    val note: String?,

    val isFavourite: Boolean,

    val created: LocalDate,

    val updated: LocalDate,

    val clearness: Float?,

    val happiness: Float?
) : IModelMappable<Dream> {

    override fun toModel() = Dream(
        dreamId, description, note, isFavourite, created, updated, clearness, happiness
    )

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
