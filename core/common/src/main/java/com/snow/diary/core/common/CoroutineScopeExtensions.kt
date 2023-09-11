package com.snow.diary.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchInBackground(
    block: suspend CoroutineScope.() -> Unit
): Job = launch(
    context = Dispatchers.IO,
    block = block
)