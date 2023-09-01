package com.snow.diary.domain.action;

import kotlinx.coroutines.flow.Flow

abstract class FlowAction<in Input, out Output> {

    protected abstract suspend fun Input.createFlow(): Flow<Output>

    suspend operator fun invoke(input: Input): Flow<Output> = input
        .createFlow()

}