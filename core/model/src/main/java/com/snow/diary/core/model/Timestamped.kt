package com.snow.diary.core.model

import java.time.LocalDate

interface Timestamped {

    val created: LocalDate

    val updated: LocalDate

}