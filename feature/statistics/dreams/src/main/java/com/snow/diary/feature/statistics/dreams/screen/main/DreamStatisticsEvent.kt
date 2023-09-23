package com.snow.diary.feature.statistics.dreams.screen.main;

import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraphPeriod

internal sealed class DreamStatisticsEvent {

    data class ChangeGraphPeriod(
        val period: DreamAmountGraphPeriod
    ): DreamStatisticsEvent()

}