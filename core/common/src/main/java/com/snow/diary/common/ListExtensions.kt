package com.snow.diary.common

import com.snow.diary.model.sort.SortDirection

fun <T> List<T>.sortedDirectional(
    direction: SortDirection
) = when (direction) {
    SortDirection.Ascending -> this
    else -> reversed()
}