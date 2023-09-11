package com.snow.diary.core.model.data

import com.snow.diary.core.model.Searchable

data class Location(

    val id: Long? = null,

    val name: String,

    val coordinates: Coordinates,

    val notes: String?

): Searchable {

    override fun getStringFields(): List<String> = listOf(
        name, notes ?: ""
    )

}
