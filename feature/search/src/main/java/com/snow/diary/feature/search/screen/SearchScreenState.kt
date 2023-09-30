package com.snow.diary.feature.search.screen;

import androidx.annotation.StringRes
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.ui.component.StatisticsDateRanges
import com.snow.diary.feature.search.R

enum class SearchTabs(
    @StringRes val title: Int,
    @StringRes val queryHint: Int
) {

    Dreams(
        R.string.search_dreams,
        R.string.search_dreams_hint
    ),

    Persons(
        R.string.search_persons,
        R.string.search_persons_hint
    ),

    Locations(
        R.string.search_locations,
        R.string.search_locations_hint
    );

}

internal sealed class SearchState {

    data object NoQuery: SearchState()

    data object Searching: SearchState()

    data object NoResults: SearchState()

    //While success can technically hold all three types, only one of the lists will ever be filled. This is made for simplicity, as the only objects ever shown to the user
    //are those correlating to the current tab.
    data class Success(
        val dreams: List<Dream>,
        val persons: List<PersonWithRelations>,
        val locations: List<Location>
    ): SearchState()

}

internal data class SearchUiState(

    val query: String = "",

    val range: StatisticsDateRanges = StatisticsDateRanges.AllTime,

    val showRangeDialog: Boolean = false,

    val selectedTab: SearchTabs

)

