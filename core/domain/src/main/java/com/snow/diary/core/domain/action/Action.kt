package com.snow.diary.core.domain.action;

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class Action<in Input, out Output> {

    protected abstract suspend fun Input.compose(): Output

    private val mutex = Mutex()

    suspend operator fun invoke(input: Input): Output = mutex.withLock {
        input.compose()
    }

}