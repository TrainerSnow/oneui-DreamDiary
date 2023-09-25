package com.snow.diary.feature.statistics.screen.dream.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.time.TimeFormat.formatFullDescription
import com.snow.diary.core.ui.graph.line.AxisLabelFitting
import com.snow.diary.core.ui.graph.line.AxisLineConfig
import com.snow.diary.core.ui.graph.line.LineGraph
import com.snow.diary.core.ui.graph.line.LineGraphFrame
import com.snow.diary.feature.statistics.R
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox
import java.time.LocalDate
import java.time.Period


internal sealed class DreamAmountGraphState {

    data object NoData : DreamAmountGraphState()

    data object Loading : DreamAmountGraphState()

    data class Success(
        val dreamAmounts: List<Int>,
        val timeStamps: List<LocalDate>
    ) : DreamAmountGraphState()

}

internal enum class DreamAmountGraphPeriod(
    @StringRes val displayName: Int,
    val period: Period
) {

    Week(R.string.stats_dreams_graph_period_week, Period.ofWeeks(1)),

    Month(R.string.stats_dreams_graph_period_month, Period.ofMonths(1)),

    Year(R.string.stats_dreams_graph_period_year, Period.ofYears(1))

}

@Composable
internal fun DreamAmountGraph(
    modifier: Modifier = Modifier,
    state: DreamAmountGraphState,
    period: DreamAmountGraphPeriod,
    onPeriodChange: (DreamAmountGraphPeriod) -> Unit
) {
    val titleStyle = TextStyle(
        fontSize = 21.sp,
        fontWeight = FontWeight.SemiBold,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val periodStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val axisLabelTextStyle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Light,
        color = OneUITheme.colors.seslSecondaryTextColor
    )
    val errorTextStyle = TextStyle(
        fontSize = 13.sp,
        color = OneUITheme.colors.seslPrimaryTextColor
    )

    RoundedCornerBox(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement
                .spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.stats_dreams_graph_title),
                    style = titleStyle
                )
                Text(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            onPeriodChange(
                                DreamAmountGraphPeriod
                                    .entries
                                    .run {
                                        getOrNull(indexOf(period) + 1) ?: first()
                                    }
                            )
                        }
                        .padding(DreamAmountGraphDefaults.periodButtonPadding),
                    text = stringResource(period.displayName),
                    style = periodStyle
                )
            }

            Box(
                modifier = Modifier
                    .height(DreamAmountGraphDefaults.height)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    DreamAmountGraphState.Loading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement
                                .spacedBy(8.dp)
                        ) {
                            ProgressIndicator(
                                type = ProgressIndicatorType.CircularIndeterminate(
                                    size = CircularProgressIndicatorSize.Companion.Large
                                )
                            )
                            Text(
                                text = stringResource(R.string.stats_dreams_loading),
                                style = errorTextStyle
                            )
                        }
                    }

                    DreamAmountGraphState.NoData -> {
                        Text(
                            text = stringResource(R.string.stats_dreams_no_data_available),
                            style = errorTextStyle
                        )
                    }

                    is DreamAmountGraphState.Success -> {
                        LineGraphFrame(
                            modifier = Modifier
                                .fillMaxSize(),
                            yLabels = (0..state.dreamAmounts.max()).toList().map(Int::toString),
                            yFitting = AxisLabelFitting.Fitting(50.dp),
                            yAxisLineConfig = AxisLineConfig(
                                color = OneUITheme.colors.seslListDividerColor
                            ),
                            yLabelTextStyle = axisLabelTextStyle,
                            xLabels = state.timeStamps.map { it.formatFullDescription() },
                            xFitting = AxisLabelFitting.Fitting(50.dp),
                            xAxisLineConfig = AxisLineConfig(
                                color = OneUITheme.colors.seslListDividerColor
                            ),
                            xLabelTextStyle = axisLabelTextStyle
                        ) {
                            LineGraph(
                                modifier = Modifier
                                    .fillMaxSize(),
                                values = state.dreamAmounts.map(Int::toFloat),
                                lineColor = OneUITheme.colors.seslPrimaryColor
                            )
                        }
                    }
                }
            }
        }
    }
}

private object DreamAmountGraphDefaults {

    val height = 300.dp

    val periodButtonPadding = PaddingValues(
        horizontal = 8.dp,
        vertical = 4.dp
    )

}

@Preview
@Composable
private fun DreamAmountGraphPreviewSuccess() {
    DreamAmountGraph(
        modifier = Modifier
            .fillMaxWidth(),
        state = DreamAmountGraphState.Success(
            dreamAmounts = listOf(4, 3, 7, 2, 1, 4, 3),
            timeStamps = (1..7).map { LocalDate.of(2023, it, 1) }
        ),
        period = DreamAmountGraphPeriod.Month,
        onPeriodChange = { }
    )
}

@Preview
@Composable
private fun DreamAmountGraphPreviewLoading() {
    DreamAmountGraph(
        modifier = Modifier
            .fillMaxWidth(),
        state = DreamAmountGraphState.Loading,
        period = DreamAmountGraphPeriod.Month,
        onPeriodChange = { }
    )
}

@Preview
@Composable
private fun DreamAmountGraphPreviewNoData() {
    DreamAmountGraph(
        modifier = Modifier
            .fillMaxWidth(),
        state = DreamAmountGraphState.NoData,
        period = DreamAmountGraphPeriod.Month,
        onPeriodChange = { }
    )
}