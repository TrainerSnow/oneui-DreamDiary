package com.snow.diary.core.common.time

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

object TimeFormat {

    fun LocalDate.formatFullDescription(): String = format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    )

    fun DayOfWeek.formatFullName(): String =
        getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )

}