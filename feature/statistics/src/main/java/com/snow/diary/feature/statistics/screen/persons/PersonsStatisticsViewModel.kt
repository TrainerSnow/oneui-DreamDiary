package com.snow.diary.feature.statistics.screen.persons;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.statistics.PersonsWithAmount
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.feature.statistics.StatisticsDateRanges
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import com.snow.diary.feature.statistics.screen.persons.components.PersonsAmountData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PersonsStatisticsViewModel @Inject constructor(
    allPersons: AllPersons,
    personsWithAmount: PersonsWithAmount
) : EventViewModel<PersonsStatisticsEvent>() {

    private val _range = MutableStateFlow(StatisticsDateRanges.AllTime)
    val range: StateFlow<StatisticsDateRanges> = _range

    private val _showRangeDialog = MutableStateFlow(false)
    val showRangeDialog: StateFlow<Boolean> = _showRangeDialog


    val amountState: StateFlow<StatisticsState<PersonsAmountData>> = combine(
        flow = allPersons(AllPersons.Input()),
        flow2 = personsWithAmount(range.value.range)
    ) { allPersons, personsWithAmount ->
        if (personsWithAmount.size < 3) return@combine StatisticsState.NoData()

        val size = allPersons.size
        val best = personsWithAmount.sortedByDescending { it.amount }.take(3)
        val total = best.sumOf { it.amount }

        return@combine StatisticsState.from(
            PersonsAmountData(
                personsNum = size,
                usedPersonsNum = total,
                most = best.map { it.person to it.amount }
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsState.Loading()
    )

    override suspend fun handleEvent(event: PersonsStatisticsEvent) = when (event) {
        is PersonsStatisticsEvent.ChangeRange -> TODO()
        PersonsStatisticsEvent.ToggleRangeDialog -> TODO()
    }

    private fun updateRange(range: StatisticsDateRanges) = viewModelScope.launch {
        _range.emit(range)
    }

    private fun toggleRangeDialog() = viewModelScope.launch {
        _showRangeDialog.emit(!showRangeDialog.value)
    }

}