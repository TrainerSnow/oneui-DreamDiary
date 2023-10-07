package com.snow.diary.feature.statistics.screen.dream

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.statistics.ClearnessAverage
import com.snow.diary.core.domain.action.statistics.DreamAmountAverage
import com.snow.diary.core.domain.action.statistics.DreamAmounts
import com.snow.diary.core.domain.action.statistics.HappinessAverage
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.ui.component.StatisticsDateRanges
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmountData
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmountGraphPeriod
import com.snow.diary.feature.statistics.screen.dream.components.DreamGraphData
import com.snow.diary.feature.statistics.screen.dream.components.DreamMetricData
import com.snow.diary.feature.statistics.screen.dream.components.DreamWeekdayData
import com.snow.diary.feature.statistics.screen.dream.components.DreamWeekdayInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
internal class DreamStatisticsViewModel @Inject constructor(
    allDreams: AllDreams,
    dreamAmountAverage: DreamAmountAverage,
    dreamAmounts: DreamAmounts,
    clearnessAverage: ClearnessAverage,
    happinessAverage: HappinessAverage
) : EventViewModel<DreamStatisticsEvent>() {

    private val _period = MutableStateFlow(DreamAmountGraphPeriod.Week)
    val period: StateFlow<DreamAmountGraphPeriod> = _period

    private val _range = MutableStateFlow(StatisticsDateRanges.AllTime)
    val range: StateFlow<StatisticsDateRanges> = _range

    private val _uiState = MutableStateFlow(DreamStatisticsUiState())
    val uiState: StateFlow<DreamStatisticsUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val amountState: StateFlow<StatisticsState<DreamAmountData>> = range
        .flatMapMerge { range ->
            amountState(range.range, allDreams, dreamAmountAverage)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatisticsState.Loading()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val amountGraphState: StateFlow<StatisticsState<DreamGraphData>> = combine(
        flow = period,
        flow2 = range
    ) { period, range ->
        period to range
    }.flatMapMerge { tuple ->
        amountGraphState(
            tuple.second.range,
            tuple.first.period,
            dreamAmounts
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    val metricState = combine(
        flow = clearnessAverage(range.value.range),
        flow2 = happinessAverage(range.value.range)
    ) { clearness, happiness ->
        if (clearness == null || happiness == null) StatisticsState.NoData()
        else StatisticsState.from(
            DreamMetricData(happiness, clearness)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    val weekdayState = allDreams(
        AllDreams.Input(dateRange = range.value.range)
    ).map {
        weekdayState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override suspend fun handleEvent(event: DreamStatisticsEvent) = when (event) {
        is DreamStatisticsEvent.ChangeGraphPeriod -> _period.emit(event.period)
        DreamStatisticsEvent.ToggleRangeDialog -> updateShowRangeDialog()
        is DreamStatisticsEvent.ChangeDateRange -> _range.emit(event.ranges)
    }

    private fun updateShowRangeDialog() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                showRangeDialog = !it.showRangeDialog
            )
        }
    }

}

private fun amountState(
    range: DateRange,
    allDreams: AllDreams,
    dreamAmountAverage: DreamAmountAverage
): Flow<StatisticsState<DreamAmountData>> = combine(
    flow = allDreams(AllDreams.Input(dateRange = range)),
    flow2 = dreamAmountAverage(range)
) { dreams, avg ->
    StatisticsState.from(
        DreamAmountData(
            amount = dreams.size,
            average = avg
        )
    )
}

private fun amountGraphState(
    range: DateRange,
    period: Period,
    dreamAmounts: DreamAmounts
): Flow<StatisticsState<DreamGraphData>> = dreamAmounts(
    DreamAmounts.Input(
        range = range,
        period = period,
        lastStart = LocalDate.now()
    )
).map {
    if (it.size < 3) StatisticsState.NoData()
    else StatisticsState.from(
        DreamGraphData(
            it.map { it.second },
            it.map { it.first }
        )
    )
}

private fun weekdayState(
    dreams: List<Dream>
): StatisticsState<DreamWeekdayData> {
    val grouping = dreams
        .groupingBy { it.created.dayOfWeek }
        .eachCount()

    val max = grouping.maxByOrNull { it.value } ?: return StatisticsState.NoData()

    return StatisticsState.from(
        DreamWeekdayData(
            weekdays = grouping.map {
                DreamWeekdayInformation(
                    it.key,
                    it.value,
                    it.value.toFloat() / dreams.size.toFloat()
                )
            },
            mostDreamsOn = max.key
        )
    )
}