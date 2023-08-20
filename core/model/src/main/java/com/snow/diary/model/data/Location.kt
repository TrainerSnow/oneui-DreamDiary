package com.snow.diary.model.data

import android.graphics.PointF
import com.snow.diary.model.Searchable

data class Location(

    val id: Long? = null,

    val name: String,

    val coordinates: PointF,

    val notes: String

): Searchable {

    override fun getStringFields(): List<String> = listOf(
        name, notes
    )

}
