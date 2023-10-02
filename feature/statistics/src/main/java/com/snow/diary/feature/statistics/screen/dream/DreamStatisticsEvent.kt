package com.snow.diary.feature.statistics.screen.dream

import com.snow.diary.core.ui.component.StatisticsDateRanges
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmountGraphPeriod

internal sealed class DreamStatisticsEvent {

    data class ChangeGraphPeriod(
        val period: DreamAmountGraphPeriod
    ): DreamStatisticsEvent()

    data class ChangeDateRange(
        val ranges: StatisticsDateRanges
    ): DreamStatisticsEvent()

    data object ToggleRangeDialog: DreamStatisticsEvent()

}