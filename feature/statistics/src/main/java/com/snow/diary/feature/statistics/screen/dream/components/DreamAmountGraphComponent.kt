package com.snow.diary.feature.statistics.screen.dream.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.time.TimeFormat.formatFullDescription
import com.snow.diary.core.ui.graph.line.AxisLabelFitting
import com.snow.diary.core.ui.graph.line.AxisLineConfig
import com.snow.diary.core.ui.graph.line.LineGraph
import com.snow.diary.core.ui.graph.line.LineGraphFrame
import com.snow.diary.feature.statistics.R
import com.snow.diary.feature.statistics.screen.components.StatisticsComponent
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import org.oneui.compose.theme.OneUITheme
import java.time.LocalDate
import java.time.Period


internal data class DreamGraphData(

    val dreamAmounts: List<Int>,

    val timeStamps: List<LocalDate>

)

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
    state: StatisticsState<DreamGraphData>,
    period: DreamAmountGraphPeriod,
    onPeriodChange: (DreamAmountGraphPeriod) -> Unit
) {
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

    StatisticsComponent(
        modifier = modifier,
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.stats_dreams_graph_title))
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
        },
        state = state
    ) { data ->
        LineGraphFrame(
            modifier = Modifier
                .fillMaxSize(),
            yLabels = (0..data.dreamAmounts.max()).toList().map(Int::toString),
            yFitting = AxisLabelFitting.Fitting(50.dp),
            yAxisLineConfig = AxisLineConfig(
                color = OneUITheme.colors.seslListDividerColor
            ),
            yLabelTextStyle = axisLabelTextStyle,
            xLabels = data.timeStamps.map { it.formatFullDescription() },
            xFitting = AxisLabelFitting.Fitting(50.dp),
            xAxisLineConfig = AxisLineConfig(
                color = OneUITheme.colors.seslListDividerColor
            ),
            xLabelTextStyle = axisLabelTextStyle
        ) {
            LineGraph(
                modifier = Modifier
                    .fillMaxSize(),
                values = data.dreamAmounts.map(Int::toFloat),
                lineColor = OneUITheme.colors.seslPrimaryColor
            )
        }
    }
}

private object DreamAmountGraphDefaults {

    val periodButtonPadding = PaddingValues(
        horizontal = 8.dp,
        vertical = 4.dp
    )

}