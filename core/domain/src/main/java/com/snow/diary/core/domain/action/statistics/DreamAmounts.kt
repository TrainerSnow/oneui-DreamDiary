package com.snow.diary.core.domain.action.statistics

import android.util.Log.d
import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.time.FirstDreamDate
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.Period

class DreamAmounts(
    private val allDreams: AllDreams,
    private val firstDreamDate: FirstDreamDate
) : FlowAction<DreamAmounts.Input, List<Pair<LocalDate, Int>>>() {

    data class Input(
        val range: DateRange = DateRange.AllTime,
        val period: Period = Period.ofMonths(1),
        val totalEnd: LocalDate,
        val firstDate: LocalDate? = null
    )

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
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
        flow2 = firstDreamDate(Unit).map { date ->
            if(date == null) return@map null
            firstDate?.coerceAtLeast(date) ?: date
        }
    ) { dreams, firstDate ->
        d("DreamAmounts", "Got dreams with size  =${dreams.size} and firstDate = $firstDate")
        if (dreams.isEmpty() || firstDate == null) return@combine emptyList<Pair<LocalDate, Int>>()

        val amounts = mutableListOf<Pair<LocalDate, Int>>()
        var start = firstDate!! //Inclusive
        var end = firstDate.plus(period) //Exclusive
        d("DreamAmounts", "Created initial start = $start and end = $end")

        while (end.isBefore(totalEnd)) {
            val dreamsInRange = dreams
                .filter {
                    (it.created.isAfter(start) || it.created == start) && it.created.isBefore(end)
                }
            d("DreamAmounts", "Found ${dreamsInRange.size} dreams in range $start to $end")

            amounts.add(start to dreamsInRange.size)

            start = end
            end = start.plus(period)
            d("DreamAmounts", "Adjusted start to $start and end to $end")
        }

        val lastAmount = dreams.filter {
            it.created.isAfter(start) && it.created.isBefore(end)
        }.size
        d("DreamAmounts", "Found last dream in range $start to $end : $lastAmount")

        amounts.add(start to lastAmount)

        d("DreamAmounts", "Will return amount: $amounts")
        return@combine amounts
    }


}