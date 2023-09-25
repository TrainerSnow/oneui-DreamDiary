package com.snow.diary.feature.statistics.dreams.screen.main;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.statistics.ClearnessAverage
import com.snow.diary.core.domain.action.statistics.DreamAmountAverage
import com.snow.diary.core.domain.action.statistics.DreamAmounts
import com.snow.diary.core.domain.action.statistics.HappinessAverage
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.feature.statistics.dreams.StatisticsDateRanges
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraphPeriod
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraphState
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountState
import com.snow.diary.feature.statistics.dreams.screen.components.DreamMetricState
import com.snow.diary.feature.statistics.dreams.screen.components.DreamWeekdayInformation
import com.snow.diary.feature.statistics.dreams.screen.components.DreamWeekdayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val amountState = combine(
        flow = allDreams(
            AllDreams.Input(
                dateRange = range.value.range
            )
        ),
        flow2 = dreamAmountAverage(range.value.range)
    ) { dreams, avg ->
        DreamAmountState.Success(
            amount = dreams.size,
            monthlyAverage = avg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DreamAmountState.Loading
    )

    val amountGraphState = dreamAmounts(
        DreamAmounts.Input(
            range = range.value.range,
            period = period.value.period,
            totalEnd = LocalDate.now()
        )
    ).map {
        if (it.size < 3) return@map DreamAmountGraphState.NoData

        return@map DreamAmountGraphState.Success(
            dreamAmounts = it.map { it.second },
            timeStamps = it.map { it.first }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DreamAmountGraphState.Loading
    )

    val metricState = combine(
        flow = clearnessAverage(range.value.range),
        flow2 = happinessAverage(range.value.range)
    ) { clearness, happiness ->
        if (clearness == null || happiness == null) DreamMetricState.NoDate
        else DreamMetricState.Success(happiness, clearness)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DreamMetricState.Loading
    )

    val weekdayState = allDreams(
        AllDreams.Input(dateRange = range.value.range)
    ).map {
        println("asdjasddsa 1")
        val mappedAmounts = mutableMapOf<DayOfWeek, Int>()
        println("asdjasddsa 2")
        val total = it.size
        println("asdjasddsa 3")
        var max: Pair<DayOfWeek, Int>? = null
        println("asdjasddsa 4")
        it.forEach { dream ->
            val dow = dream.created.dayOfWeek
            mappedAmounts[dow] =
                if (dow in mappedAmounts.keys) mappedAmounts[dow]!! + 1
                else 1
            if (mappedAmounts[dow]!! > (max?.second ?: 0)) max = dow to mappedAmounts[dow]!!
        }
        println("asdjasddsa 5")

        if (max == null) return@map DreamWeekdayState.NoData
        println("asdjasddsa 6")

        return@map DreamWeekdayState.Success(
            weekdays = mappedAmounts.map {
                DreamWeekdayInformation(
                    it.key,
                    it.value,
                    it.value.toFloat() / total.toFloat()
                )
            },
            mostDreamsOn = max!!.first
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DreamWeekdayState.Loading
    )

    override suspend fun handleEvent(event: DreamStatisticsEvent) = when (event) {
        is DreamStatisticsEvent.ChangeGraphPeriod -> TODO()
        DreamStatisticsEvent.ToggleRangeDialog -> TODO()
        is DreamStatisticsEvent.ChangeDateRange -> TODO()
    }

    private fun updatePeriod(period: DreamAmountGraphPeriod) = viewModelScope.launch {
        _period.emit(period)
    }

}