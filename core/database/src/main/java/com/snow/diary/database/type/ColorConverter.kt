package com.snow.diary.database.type;

import android.graphics.Color
import androidx.room.TypeConverter

class ColorConverter {

    @TypeConverter
    fun colorToInt(color: Color?): Int? = color?.toArgb()

    @TypeConverter
    fun intToColor(int: Int?): Color? = int?.let {
        Color.valueOf(it)
    }

    companion object {

        private const val SEPARATOR = "|"

    }

}