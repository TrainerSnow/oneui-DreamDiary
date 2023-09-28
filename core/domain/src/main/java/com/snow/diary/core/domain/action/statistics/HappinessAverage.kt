package com.snow.diary.core.domain.action.statistics;

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.pure.happinessAverage
import com.snow.diary.core.model.data.Dream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HappinessAverage(
    private val allDreams: AllDreams
): FlowAction<DateRange, Float?>() {

    override fun DateRange.createFlow(): Flow<Float?> = allDreams(
        AllDreams.Input(dateRange = this)
    ).map(List<Dream>::happinessAverage)

}