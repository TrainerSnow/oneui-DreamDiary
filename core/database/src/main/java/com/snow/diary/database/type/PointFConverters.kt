package com.snow.diary.database.type;

import android.graphics.PointF
import androidx.room.TypeConverter

class PointFConverters {

    @TypeConverter
    fun pointFToString(point: PointF?) = point?.let {
        "${it.x}$SEPARATOR${it.y}"
    }

    @TypeConverter
    fun stringToPointF(string: String?): PointF? = string?.let {
        return PointF(
            string.split(SEPARATOR).component1().toFloat(),
            string.split(SEPARATOR).component2().toFloat()
        )
    }


    companion object {
        private const val SEPARATOR = "|"
    }
}