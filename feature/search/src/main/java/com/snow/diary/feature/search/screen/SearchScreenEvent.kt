package com.snow.diary.feature.search.screen

import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.ui.component.StatisticsDateRanges

internal sealed class SearchEvent {

    data class ChangeQuery(
        val query: String
    ): SearchEvent()

    data class ChangeTab(
        val tab: SearchTabs
    ): SearchEvent()

    data object ToggleRangeDialog: SearchEvent()

    data class ChangeRange(
        val range: StatisticsDateRanges
    ): SearchEvent()

    data class DreamFavouriteClick(
        val dream: Dream
    ): SearchEvent()

    data class PersonFavouriteClick(
        val person: Person
    ): SearchEvent()

}