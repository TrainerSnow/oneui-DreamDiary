package com.snow.diary.core.common

import com.snow.diary.core.model.sort.SortDirection

fun <T> List<T>.sortedDirectional(
    direction: SortDirection
) = when (direction) {
    SortDirection.Ascending -> this
    else -> reversed()
}