package com.snow.diary.core.domain.pure

import com.snow.diary.core.model.data.Dream

fun List<Dream>.happinessAverage(): Float? =
    mapNotNull(Dream::happiness)
        .run {
            if(isEmpty()) null
            else average().toFloat()
        }

fun List<Dream>.clearnessAverage(): Float? =
    mapNotNull(Dream::clearness)
        .run {
            if(isEmpty()) null
            else average().toFloat()
        }