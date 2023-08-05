package com.snow.diary.model.data

import java.time.LocalDate

data class Dream(

    val id: Long,

    val description: String,

    val note: String?,

    val created: LocalDate,

    val updated: LocalDate,

    val clearness: Float?,

    val happiness: Float?
)
