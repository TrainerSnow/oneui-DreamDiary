package com.snow.diary.domain.action;

abstract class Action<in Input, out Output> {

    protected abstract suspend fun Input.compose(): Output

    suspend operator fun invoke(input: Input): Output = input.compose()

}