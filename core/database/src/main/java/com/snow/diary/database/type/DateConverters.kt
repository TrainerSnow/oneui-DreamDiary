package com.snow.diary.database.type;

import androidx.room.TypeConverter
import java.time.Instant
import java.util.Date

class DateConverters {

    @TypeConverter
    fun dateToMillis(date: Date?): Long? = date?.time

    @TypeConverter
    fun millisToDate(millis: Long?): Date? = millis?.let {
        Date(millis)
    }

}