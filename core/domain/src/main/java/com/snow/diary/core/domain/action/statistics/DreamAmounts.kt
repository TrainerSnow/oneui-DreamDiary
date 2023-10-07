package com.snow.diary.core.domain.action.statistics

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.time.FirstDreamDate
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.Period

class DreamAmounts(
    private val allDreams: AllDreams,
    private val firstDreamDate: FirstDreamDate
) : FlowAction<DreamAmounts.Input, List<Pair<LocalDate, Int>>>() {

    data class Input(
        val range: DateRange = DateRange.AllTime,
        val period: Period = Period.ofMonths(1),
        val lastStart: LocalDate
    )

    override fun Input.createFlow(): Flow<List<Pair<LocalDate, Int>>> = combine(
        flow = allDreams(
            AllDreams.Input(
                sortConfig = SortConfig(
                    mode = SortMode.Created,
                    direction = SortDirection.Ascending
                ),
                dateRange = range
            )
        ),
        flow2 = firstDreamDate(Unit)
    ) { dreams, firstDreamDate ->
        if (dreams.isEmpty() || firstDreamDate == null) return@combine emptyList<Pair<LocalDate, Int>>()

        val startDate = if (range.resolve().from.isAfter(firstDreamDate)) range.resolve().from
        else firstDreamDate

        val amounts = mutableListOf<Pair<LocalDate, Int>>()
        var start = startDate //Inclusive
        var end = startDate.plus(period) //Exclusive

        while (!start.isAfter(lastStart)) {
            val dreamsInRange = dreams
                .filter {
                    (it.created.isAfter(start) || it.created == start) && it.created.isBefore(end)
                }

            amounts.add(start to dreamsInRange.size)

            start = end
            end = start.plus(period)
        }

        return@combine amounts
    }


}