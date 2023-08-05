package com.snow.diary.model.data

import java.util.Date

data class Dream(

    val id: Long,

    val description: String,

    val note: String?,

    val created: Date,

    val updated: Date,

    val clearness: Float?,

    val happiness: Float?
)
