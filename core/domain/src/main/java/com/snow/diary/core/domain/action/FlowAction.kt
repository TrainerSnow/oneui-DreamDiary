package com.snow.diary.core.domain.action

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex

abstract class FlowAction<in Input, out Output> {

    protected abstract fun Input.createFlow(): Flow<Output>

    private val mutex = Mutex()

    operator fun invoke(input: Input): Flow<Output> = synchronized(this) {
        input
            .createFlow()
    }

}