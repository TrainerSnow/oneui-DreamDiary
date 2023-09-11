package com.snow.diary.core.model.data

import com.snow.diary.core.model.Searchable

data class Person(

    val id: Long? = null,

    val name: String,

    val isFavourite: Boolean,

    val relationId: Long,

    val notes: String?

): Searchable {

    override fun getStringFields(): List<String> = listOf(
        name, notes ?: ""
    )

}