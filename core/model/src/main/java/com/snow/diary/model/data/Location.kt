package com.snow.diary.model.data

import com.snow.diary.model.Searchable

data class Location(

    val id: Long? = null,

    val name: String,

    val coordinates: Coordinates,

    val notes: String

): Searchable {

    override fun getStringFields(): List<String> = listOf(
        name, notes
    )

}
