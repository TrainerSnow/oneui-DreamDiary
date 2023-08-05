package com.snow.diary.common

inline fun <T, reified R> Array<out T>.map(
    action: (T) -> R
): Array<out R> = toList()
    .map(action)
    .toTypedArray()