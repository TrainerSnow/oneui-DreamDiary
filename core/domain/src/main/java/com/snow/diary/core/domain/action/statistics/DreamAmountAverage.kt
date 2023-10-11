package com.snow.diary.core.domain.action.statistics

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.time.FirstDreamDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.Period

class DreamAmountAverage(
    private val allDreams: AllDreams,
    private val firstDreamDate: FirstDreamDate
) : FlowAction<DateRange, Float>() {

    override fun DateRange.createFlow(): Flow<Float> = combine(
        flow = allDreams(
            AllDreams.Input(
                dateRange = this
            )
        ),
        flow2 = firstDreamDate(Unit)
    ) { dreams, firstDate ->
        val fixed = resolve()

        val from = firstDate?.let { fixed.from.coerceAtLeast(firstDate) }
        val to = fixed.to.coerceAtMost(LocalDate.now())

        val period = Period.between(from, to)
        val months = period.toTotalMonths()

        val avg = if (months > 0) dreams.size.toFloat() / months.toFloat()
        else (dreams.size.toFloat() / period.days.toFloat()) * 30

        avg
    }

}