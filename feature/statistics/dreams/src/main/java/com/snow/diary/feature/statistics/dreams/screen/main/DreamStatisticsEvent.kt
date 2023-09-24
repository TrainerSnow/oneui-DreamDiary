package com.snow.diary.feature.statistics.dreams.screen.main;

import com.snow.diary.feature.statistics.dreams.StatisticsDateRanges
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraphPeriod

internal sealed class DreamStatisticsEvent {

    data class ChangeGraphPeriod(
        val period: DreamAmountGraphPeriod
    ): DreamStatisticsEvent()

    data class ChangeDateRange(
        val ranges: StatisticsDateRanges
    ): DreamStatisticsEvent()

    data object ToggleRangeDialog: DreamStatisticsEvent()

}