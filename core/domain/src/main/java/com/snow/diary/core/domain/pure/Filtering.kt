package com.snow.diary.core.domain.pure

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.model.Timestamped

fun <T : Timestamped> List<T>.filterRange(
    dateRange: DateRange
): List<T> = filter { obj ->
    obj.created in dateRange
}