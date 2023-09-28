package com.snow.diary.feature.statistics.screen.persons;

import com.snow.diary.feature.statistics.StatisticsDateRanges

internal sealed class PersonsStatisticsEvent {

    data class ChangeRange(
        val range: StatisticsDateRanges
    ): PersonsStatisticsEvent()

    data object ToggleRangeDialog: PersonsStatisticsEvent()

}