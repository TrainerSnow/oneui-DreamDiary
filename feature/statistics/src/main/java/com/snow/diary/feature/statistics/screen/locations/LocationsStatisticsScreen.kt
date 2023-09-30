package com.snow.diary.feature.statistics.screen.locations

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
import com.snow.diary.core.ui.component.DateRangeDialog
import com.snow.diary.feature.statistics.R
import com.snow.diary.core.ui.component.StatisticsDateRanges
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import com.snow.diary.feature.statistics.screen.locations.components.LocationsAmount
import com.snow.diary.feature.statistics.screen.locations.components.LocationsAmountData
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.widgets.buttons.IconButton

@Composable
internal fun LocationsStatistics(
    viewModel: LocationsStatisticsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val amountState by viewModel.amountState.collectAsStateWithLifecycle()
    val range by viewModel.range.collectAsStateWithLifecycle()
    val showDialog by viewModel.showRangeDialog.collectAsStateWithLifecycle()

    LocationsStatistics(
        amountState = amountState,
        range = range,
        showRangeDialog = showDialog,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun LocationsStatistics(
    amountState: StatisticsState<LocationsAmountData>,
    range: StatisticsDateRanges,
    showRangeDialog: Boolean,
    onEvent: (LocationsStatisticsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    if (showRangeDialog) {
        DateRangeDialog(
            onDismissRequest = {
                onEvent(LocationsStatisticsEvent.ToggleRangeDialog)
            },
            selected = range,
            onRangeSelect = {
                onEvent(
                    LocationsStatisticsEvent.ChangeRange(it)
                )
            }
        )
    }

    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED),
        toolbarTitle = stringResource(R.string.stats_locations_main_title),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(dev.oneuiproject.oneui.R.drawable.ic_oui_drawer),
                onClick = onNavigateBack
            )
        },
        appbarActions = {
            IconButton(
                icon = Icon.Resource(dev.oneuiproject.oneui.R.drawable.ic_oui_calendar_month),
                onClick = {
                    onEvent(
                        LocationsStatisticsEvent.ToggleRangeDialog
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
                LocationsAmount(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = amountState
                )
            }
        }
    }
}