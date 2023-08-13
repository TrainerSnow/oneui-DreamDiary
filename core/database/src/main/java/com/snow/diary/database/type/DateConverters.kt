package com.snow.diary.database.type;

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverters {

    @TypeConverter
    fun dateToIso(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun isoToDate(iso: String?): LocalDate? = LocalDate
        .parse(iso)

}