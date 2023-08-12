package com.snow.diary.common.time

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object TimeFormat {

    fun LocalDate.formatFullDescription(): String = format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    )

}