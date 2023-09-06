package com.snow.diary.io.data.entity

import com.snow.diary.io.data.serialization.DateSerializer
import com.snow.diary.model.data.Dream
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DreamEntity(

    val id: Long? = null,

    val description: String,

    val note: String?,

    val isFavourite: Boolean,

    @Serializable(DateSerializer::class)
    val created: LocalDate,

    @Serializable(DateSerializer::class)
    val updated: LocalDate,

    val clearness: Float?,

    val happiness: Float?

){

    constructor(dream: Dream) : this(
        dream.id, dream.description, dream.note, dream.isFavourite, dream.created, dream.updated, dream.clearness, dream.happiness
    )

    fun toModel() = Dream(
        id, description, note, isFavourite, created, updated, clearness, happiness
    )

}
