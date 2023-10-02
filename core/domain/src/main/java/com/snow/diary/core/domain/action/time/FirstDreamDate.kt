package com.snow.diary.core.domain.action.time

import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class FirstDreamDate(
    private val allDreams: AllDreams
) : FlowAction<Unit, LocalDate?>() {
    override fun Unit.createFlow(): Flow<LocalDate?> = allDreams(
        AllDreams.Input(
            sortConfig = SortConfig(
                mode = SortMode.Created,
                direction = SortDirection.Descending
            )
        )
    )
        .map {
            it.firstOrNull()?.created
        }
}