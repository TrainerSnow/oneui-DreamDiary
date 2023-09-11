package com.snow.diary.core.model.data

import com.snow.diary.core.model.Searchable
import com.snow.diary.core.model.Timestamped
import java.time.LocalDate

data class Dream(

    val id: Long? = null,

    val description: String,

    val note: String?,

    val isFavourite: Boolean,

    override val created: LocalDate,

    override val updated: LocalDate,

    val clearness: Float?,

    val happiness: Float?

): Searchable, Timestamped {

    override fun getStringFields(): List<String> = listOf(
        description, note ?: ""
    )

}

