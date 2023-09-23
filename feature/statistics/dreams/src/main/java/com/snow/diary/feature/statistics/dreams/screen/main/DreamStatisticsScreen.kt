package com.snow.diary.feature.statistics.dreams.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.feature.statistics.dreams.R
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmount
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraph
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraphPeriod
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountGraphState
import com.snow.diary.feature.statistics.dreams.screen.components.DreamAmountState
import com.snow.diary.feature.statistics.dreams.screen.components.DreamMetricComponent
import com.snow.diary.feature.statistics.dreams.screen.components.DreamMetricState
import com.snow.diary.feature.statistics.dreams.screen.components.DreamWeekday
import com.snow.diary.feature.statistics.dreams.screen.components.DreamWeekdayInformation
import com.snow.diary.feature.statistics.dreams.screen.components.DreamWeekdayState
import org.oneui.compose.layout.drawer.DrawerLayout
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
private fun DreamStatistics(
    amountState: DreamAmountState,
    amountGraphState: DreamAmountGraphState,
    graphPeriod: DreamAmountGraphPeriod,
    metricState: DreamMetricState,
    weekdayState: DreamWeekdayState,
    onEvent: (DreamStatisticsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED),
        expandable = false,
        toolbarTitle = stringResource(R.string.stats_dreams_main_title)
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(400.dp),
            verticalArrangement = Arrangement
                .spacedBy(12.dp),
            horizontalArrangement = Arrangement
                .spacedBy(12.dp)
        ) {
            item {
                DreamAmount(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = amountState
                )
            }
            item {
                DreamAmountGraph(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = amountGraphState,
                    period = graphPeriod,
                    onPeriodChange = {
                        onEvent(
                            DreamStatisticsEvent.ChangeGraphPeriod(it)
                        )
                    }
                )
            }
            item {
                DreamMetricComponent(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = metricState
                )
            }
            item {
                DreamWeekday(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = weekdayState
                )
            }
        }
    }
}

@Preview
@Composable
private fun DreamStatisticsPreview() {
    DrawerLayout(
        drawerContent = { }
    ) {
        DreamStatistics(
            amountState = DreamAmountState.Success(
                amount = 756,
                monthlyAverage = 4.893648F
            ),
            amountGraphState = DreamAmountGraphState.Success(
                dreamAmounts = listOf(7, 8, 3, 9, 6, 1, 4, 2, 8),
                timeStamps = (1..9).map { LocalDate.of(2023, it, 1) }.toList()
            ),
            graphPeriod = DreamAmountGraphPeriod.Month,
            metricState = DreamMetricState.Success(
                happinessAverage = 0.83F,
                clearnessAverage = 0.27F
            ),
            weekdayState = DreamWeekdayState.Success(
                weekdays = listOf(
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.MONDAY,
                        totalAmount = 42,
                        percentage = 0.43F
                    ),
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.TUESDAY,
                        totalAmount = 846,
                        percentage = 0.11F
                    ),
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.WEDNESDAY,
                        totalAmount = 24,
                        percentage = 0.34F
                    ),
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.THURSDAY,
                        totalAmount = 87,
                        percentage = 0.92F
                    ),
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.FRIDAY,
                        totalAmount = 24,
                        percentage = 0.23F
                    ),
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.SATURDAY,
                        totalAmount = 11,
                        percentage = 0.1F
                    ),
                    DreamWeekdayInformation(
                        weekday = DayOfWeek.SUNDAY,
                        totalAmount = 37,
                        percentage = 0.034F
                    ),
                ),
                mostDreamsOn = DayOfWeek.SATURDAY
            ),
            onEvent = { },
            onNavigateBack = { }
        )
    }
}