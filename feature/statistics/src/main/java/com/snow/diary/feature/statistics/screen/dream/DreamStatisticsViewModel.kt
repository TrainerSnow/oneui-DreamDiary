package com.snow.diary.feature.statistics.screen.dream

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.statistics.ClearnessAverage
import com.snow.diary.core.domain.action.statistics.DreamAmountAverage
import com.snow.diary.core.domain.action.statistics.DreamAmounts
import com.snow.diary.core.domain.action.statistics.HappinessAverage
import com.snow.diary.core.domain.viewmodel.EventViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class DreamStatisticsViewModel @Inject constructor(
    allDreams: AllDreams,
    dreamAmountAverage: DreamAmountAverage,
    dreamAmounts: DreamAmounts,
    clearnessAverage: ClearnessAverage,
    happinessAverage: HappinessAverage
) : EventViewModel<DreamStatisticsEvent>() {

    private val _period = MutableStateFlow(DreamAmountGraphPeriod.Month)
    val period: StateFlow<DreamAmountGraphPeriod> = _period

    private val _range = MutableStateFlow(StatisticsDateRanges.AllTime)
    val range: StateFlow<StatisticsDateRanges> = _range

    private val _uiState = MutableStateFlow(DreamStatisticsUiState())
    val uiState: StateFlow<DreamStatisticsUiState> = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val amountState: StateFlow<StatisticsState<DreamAmountData>> = range.flatMapMerge { range ->
        combine(
            flow = allDreams(
                AllDreams.Input(
                    dateRange = range.range
                )
            ),
            flow2 = dreamAmountAverage(range.range)
        ) { dreams, avg ->
            StatisticsState.from(
                DreamAmountData(
                    amount = dreams.size,
                    average = avg
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val amountGraphState = combine(range, period) { range, period ->
        Pair(range, period)
    }.flatMapMerge { tuple ->
        dreamAmounts(
            DreamAmounts.Input(
                range = tuple.first.range,
                period = tuple.second.period,
                totalEnd = LocalDate.now(),
                firstDate = tuple.first.range.resolve().from
            )
        )
    }.map {
        if (it.size < 3) return@map StatisticsState.NoData()

        return@map StatisticsState.from(
            DreamGraphData(
                dreamAmounts = it.map { it.second },
                timeStamps = it.map { it.first }
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val metricState = range.flatMapMerge { range ->
        combine(
            flow = clearnessAverage(range.range),
            flow2 = happinessAverage(range.range)
        ) { clearness, happiness ->
            if (clearness == null || happiness == null) StatisticsState.NoData()
            else StatisticsState.from(
                DreamMetricData(happiness, clearness)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val weekdayState = range.flatMapMerge { range ->
        allDreams(
            AllDreams.Input(dateRange = range.range)
        ).map {
            val mappedAmounts = mutableMapOf<DayOfWeek, Int>()
            val total = it.size
            var max: Pair<DayOfWeek, Int>? = null
            it.forEach { dream ->
                val dow = dream.created.dayOfWeek
                mappedAmounts[dow] =
                    if (dow in mappedAmounts.keys) mappedAmounts[dow]!! + 1
                    else 1
                if (mappedAmounts[dow]!! > (max?.second ?: 0)) max = dow to mappedAmounts[dow]!!
            }

            if (max == null) return@map StatisticsState.NoData()

            return@map StatisticsState.from(
                DreamWeekdayData(
                    weekdays = mappedAmounts.map {
                        DreamWeekdayInformation(
                            it.key,
                            it.value,
                            it.value.toFloat() / total.toFloat()
                        )
                    },
                    mostDreamsOn = max!!.first
                )
            )
        }
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