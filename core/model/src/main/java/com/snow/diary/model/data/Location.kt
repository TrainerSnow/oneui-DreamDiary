package com.snow.diary.model.data

import android.graphics.PointF

data class Location(

    val id: Long,

    val name: String,

    val coordinates: PointF,

    val notes: String
)
