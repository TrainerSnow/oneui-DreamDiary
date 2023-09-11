package com.snow.diary.core.domain.action;

import kotlinx.coroutines.flow.Flow

abstract class FlowAction<in Input, out Output> {

    protected abstract fun Input.createFlow(): Flow<Output>

    operator fun invoke(input: Input): Flow<Output> = input
        .createFlow()

}