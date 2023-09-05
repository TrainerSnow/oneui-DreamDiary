package com.snow.diary.model.data

import com.snow.diary.model.Searchable

data class Relation(

    val id: Long? = null,

    val name: String,

    val color: Int

): Searchable {

    override fun getStringFields(): List<String> = listOf(
        name
    )

}