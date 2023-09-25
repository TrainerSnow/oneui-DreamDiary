package com.snow.diary.feature.statistics.screen.dream.components;

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.feature.statistics.R
import com.snow.diary.feature.statistics.screen.components.StatisticsComponent
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import org.oneui.compose.theme.OneUITheme


internal data class DreamMetricData(

    val happinessAverage: Float,

    val clearnessAverage: Float

)

@Composable
internal fun DreamMetricComponent(
    modifier: Modifier = Modifier,
    state: StatisticsState<DreamMetricData>,
    onClick: (() -> Unit)? = null
) {
    val labelStyle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val decimalFormat = DecimalFormat("#.##")
    val happinessColor = Color(0xfffcca05)
    val clearnessColor = Color(0xff63d1d2)

    StatisticsComponent(
        modifier = modifier,
        onClick = onClick,
        title = stringResource(R.string.stats_dreams_metric_title),
        state = state
    ) { data ->
        Column(
            verticalArrangement = Arrangement
                .spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(
                            with(LocalDensity.current) { labelStyle.fontSize.toDp() }
                        )
                        .width(12.dp)
                        .clip(CircleShape)
                        .background(happinessColor, CircleShape)
                )
                Text(
                    text = stringResource(
                        R.string.stats_dreams_metric_happiness,
                        decimalFormat.format(data.happinessAverage)
                    ),
                    style = labelStyle
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(
                            with(LocalDensity.current) { labelStyle.fontSize.toDp() }
                        )
                        .width(12.dp)
                        .clip(CircleShape)
                        .background(clearnessColor, CircleShape)
                )
                Text(
                    text = stringResource(
                        R.string.stats_dreams_metric_clearnesss,
                        decimalFormat.format(data.clearnessAverage)
                    ),
                    style = labelStyle
                )
            }
        }
    }
}

@Preview
@Composable
private fun DreamMetricPreview() {
    DreamMetricComponent(
        state = StatisticsState.from(
            DreamMetricData(
                happinessAverage = 0.6836F,
                clearnessAverage = 0.25937F
            )
        )
    )
}
