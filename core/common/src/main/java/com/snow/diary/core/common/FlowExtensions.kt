package com.snow.diary.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<T>.mapScoped(crossinline transform: suspend T.() -> R): Flow<R> = map {
    it.transform()
}