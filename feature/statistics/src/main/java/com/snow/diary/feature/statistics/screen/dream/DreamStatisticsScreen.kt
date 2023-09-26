package com.snow.diary.feature.statistics.screen.dream

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.feature.statistics.DateRangeDialog
import com.snow.diary.feature.statistics.R
import com.snow.diary.feature.statistics.StatisticsDateRanges
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmount
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmountData
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmountGraph
import com.snow.diary.feature.statistics.screen.dream.components.DreamAmountGraphPeriod
import com.snow.diary.feature.statistics.screen.dream.components.DreamGraphData
import com.snow.diary.feature.statistics.screen.dream.components.DreamMetricComponent
import com.snow.diary.feature.statistics.screen.dream.components.DreamMetricData
import com.snow.diary.feature.statistics.screen.dream.components.DreamWeekday
import com.snow.diary.feature.statistics.screen.dream.components.DreamWeekdayData
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun DreamStatistics(
    viewModel: DreamStatisticsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val amountState by viewModel.amountState.collectAsStateWithLifecycle()
    val amountGraphState by viewModel.amountGraphState.collectAsStateWithLifecycle()
    val graphPeriod by viewModel.period.collectAsStateWithLifecycle()
    val metricState by viewModel.metricState.collectAsStateWithLifecycle()
    val weekdayState by viewModel.weekdayState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val range by viewModel.range.collectAsStateWithLifecycle()

    DreamStatistics(
        amountState = amountState,
        amountGraphState = amountGraphState,
        graphPeriod = graphPeriod,
        metricState = metricState,
        weekdayState = weekdayState,
        uiState = uiState,
        range = range,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun DreamStatistics(
    amountState: StatisticsState<DreamAmountData>,
    amountGraphState: StatisticsState<DreamGraphData>,
    graphPeriod: DreamAmountGraphPeriod,
    metricState: StatisticsState<DreamMetricData>,
    weekdayState: StatisticsState<DreamWeekdayData>,
    uiState: DreamStatisticsUiState,
    range: StatisticsDateRanges,
    onEvent: (DreamStatisticsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    if (uiState.showRangeDialog) {
        DateRangeDialog(
            onDismissRequest = {
                onEvent(DreamStatisticsEvent.ToggleRangeDialog)
            },
            selected = range,
            onRangeSelect = {
                onEvent(
                    DreamStatisticsEvent.ChangeDateRange(it)
                )
            }
        )
    }

    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED),
        expandable = false,
        toolbarTitle = stringResource(R.string.stats_dreams_main_title),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_drawer),
                onClick = onNavigateBack
            )
        },
        appbarActions = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_calendar_month),
                onClick = {
                    onEvent(
                        DreamStatisticsEvent.ToggleRangeDialog
                    )
                }
            )
        }
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