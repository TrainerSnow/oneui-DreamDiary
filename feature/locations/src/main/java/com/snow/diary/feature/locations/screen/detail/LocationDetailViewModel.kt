package com.snow.diary.feature.locations.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.dream.DreamsFromLocation
import com.snow.diary.core.domain.action.dream.UpdateDream
import com.snow.diary.core.domain.action.location.DeleteLocation
import com.snow.diary.core.domain.action.location.LocationById
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.feature.locations.nav.LocationDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LocationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    locationById: LocationById,
    dreamsFromLocation: DreamsFromLocation,
    val updateDream: UpdateDream,
    val deleteLocation: DeleteLocation
) : EventViewModel<LocationDetailEvent>() {

    private val args = LocationDetailArgs(savedStateHandle)

    val state = locationDetailState(locationById, args.locationId, dreamsFromLocation)
        .stateIn(
            scope = viewModelScope,
            initialValue = LocationDetailState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val _tabs = MutableStateFlow(LocationDetailTab.General)
    val tabs: StateFlow<LocationDetailTab> = _tabs
    override suspend fun handleEvent(event: LocationDetailEvent) = when (event) {
        is LocationDetailEvent.ChangeTab -> changeTab(event.tab)
        is LocationDetailEvent.DreamFavouriteClick -> dreamFavouriteClick(event.dream)
        is LocationDetailEvent.Delete -> delete()
    }

    private fun changeTab(tab: LocationDetailTab) = viewModelScope.launch {
        _tabs.emit(tab)
    }

    private fun dreamFavouriteClick(dream: Dream) = viewModelScope.launchInBackground {
        updateDream(
            listOf(
                dream.copy(
                    isFavourite = !dream.isFavourite
                )
            )
        )
    }

    private fun delete() = viewModelScope.launchInBackground {
        (state.value as? LocationDetailState.Success)?.let {
            deleteLocation(it.location)
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
private fun locationDetailState(
    locationById: LocationById,
    id: Long,
    dreamsFromLocation: DreamsFromLocation
): Flow<LocationDetailState> = locationById(id)
    .flatMapMerge { location ->
        if (location == null) flowOf(LocationDetailState.Error(id = id))
        else dreamsFromLocation(
            DreamsFromLocation.Input(
                location = location,
                sortConfig = SortConfig(SortMode.Created, SortDirection.Ascending)
            )
        ).map { dreams ->
            LocationDetailState.Success(
                location,
                dreams
            )
        }
    }