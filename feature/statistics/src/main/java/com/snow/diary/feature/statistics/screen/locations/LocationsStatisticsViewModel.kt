package com.snow.diary.feature.statistics.screen.locations

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.statistics.LocationsWithAmount
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.ui.component.StatisticsDateRanges
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import com.snow.diary.feature.statistics.screen.locations.components.LocationsAmountData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LocationsStatisticsViewModel @Inject constructor(
    allLocations: AllLocations,
    locationsWithAmount: LocationsWithAmount
) : EventViewModel<LocationsStatisticsEvent>() {

    private val _range = MutableStateFlow(StatisticsDateRanges.AllTime)
    val range: StateFlow<StatisticsDateRanges> = _range

    private val _showRangeDialog = MutableStateFlow(false)
    val showRangeDialog: StateFlow<Boolean> = _showRangeDialog


    @OptIn(ExperimentalCoroutinesApi::class)
    val amountState: StateFlow<StatisticsState<LocationsAmountData>> = range.flatMapMerge { range ->
        combine(
            flow = allLocations(AllLocations.Input()),
            flow2 = locationsWithAmount(range.range)
        ) { allLocations, locationsWithAmount ->
            if (locationsWithAmount.size < 3) return@combine StatisticsState.NoData()

            val size = allLocations.size
            val best = locationsWithAmount.sortedByDescending { it.amount }.take(3)
            val total = best.sumOf { it.amount }

            return@combine StatisticsState.from(
                LocationsAmountData(
                    locationsNum = size,
                    usedLocationsNum = total,
                    most = best.map { it.location to it.amount }
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    override suspend fun handleEvent(event: LocationsStatisticsEvent) = when (event) {
        is LocationsStatisticsEvent.ChangeRange -> updateRange(event.range)
        LocationsStatisticsEvent.ToggleRangeDialog -> toggleRangeDialog()
    }

    private fun updateRange(range: StatisticsDateRanges) = viewModelScope.launch {
        _range.emit(range)
    }

    private fun toggleRangeDialog() = viewModelScope.launch {
        _showRangeDialog.emit(!showRangeDialog.value)
    }

}