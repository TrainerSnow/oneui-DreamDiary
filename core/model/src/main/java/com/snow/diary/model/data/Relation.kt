package com.snow.diary.model.data

import android.graphics.Color
import com.snow.diary.model.Searchable

data class Relation(

    val id: Long,

    val name: String,

    val color: Color

): Searchable {

    override fun getStringFields(): List<String> = listOf(
        name
    )

}