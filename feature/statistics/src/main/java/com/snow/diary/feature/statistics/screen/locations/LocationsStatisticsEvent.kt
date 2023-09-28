package com.snow.diary.feature.statistics.screen.locations;

import com.snow.diary.feature.statistics.StatisticsDateRanges

internal sealed class LocationsStatisticsEvent {

    data class ChangeRange(
        val range: StatisticsDateRanges
    ): LocationsStatisticsEvent()

    data object ToggleRangeDialog: LocationsStatisticsEvent()

}