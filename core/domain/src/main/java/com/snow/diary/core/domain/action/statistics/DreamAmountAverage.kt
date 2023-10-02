package com.snow.diary.core.domain.action.statistics

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Period

class DreamAmountAverage(
    private val allDreams: AllDreams
) : FlowAction<DateRange, Float>() {

    override fun DateRange.createFlow(): Flow<Float> = allDreams(
        AllDreams.Input(
            dateRange = this
        )
    ).map {
        val fixed = resolve()
        val period = Period.between(fixed.from, fixed.to)
        val months = period.months

        val avg = if (months > 0) it.size.toFloat() / months.toFloat()
        else (it.size.toFloat() / period.days.toFloat()) * 30

        avg
    }

}