package com.snow.diary.feature.locations.screen.list

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.feed.LocationFeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LocationsListViewModel @Inject constructor(
    allLocations: AllLocations
) : EventViewModel<LocationListEvent>() {

    private val _sortConfig = MutableStateFlow(
        SortConfig(
            mode = SortMode.Created,
            direction = SortDirection.Descending
        )
    )
    val sortConfig: StateFlow<SortConfig> = _sortConfig


    val feedState = locationListState(
        allLocations = allLocations,
        sortConfig = sortConfig
    ).stateIn(
        scope = viewModelScope,
        initialValue = LocationFeedState.Loading,
        started = SharingStarted.WhileSubscribed(5000L)
    )

    override suspend fun handleEvent(event: LocationListEvent) = when (event) {
        is LocationListEvent.SortChange -> handleSortChange(event.sortConfig)
    }

    private fun handleSortChange(sortConfig: SortConfig) = viewModelScope.launch {
        _sortConfig.emit(
            sortConfig
        )
    }


}

@OptIn(ExperimentalCoroutinesApi::class)
private fun locationListState(
    allLocations: AllLocations,
    sortConfig: StateFlow<SortConfig>
): Flow<LocationFeedState> = sortConfig
    .flatMapMerge { sort ->
        allLocations(
            AllLocations.Input(sort)
        )
    }.map { locations ->
        if (locations.isEmpty()) LocationFeedState.Empty
        else LocationFeedState.Success(locations)
    }