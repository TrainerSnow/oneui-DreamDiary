package com.snow.diary.feature.statistics.dreams.screen.components;

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.snow.diary.feature.statistics.dreams.R
import org.oneui.compose.widgets.box.RoundedCornerBox


internal sealed class DreamMetricState {

    data object Loading : DreamMetricState()

    data object NoDate : DreamMetricState()

    data class Success(

        val happinessAverage: Float,

        val clearnessAverage: Float

    ) : DreamMetricState()

}

@Composable
internal fun DreamMetricComponent(
    modifier: Modifier = Modifier,
    state: DreamMetricState,
    onClick: (() -> Unit)? = null
) {
    val labelStyle = TextStyle(
        fontSize = 19.sp
    )
    val titleStyle = TextStyle(
        fontSize = 21.sp,
        fontWeight = FontWeight.SemiBold
    )
    val decimalFormat = DecimalFormat("#.##")
    val happinessColor = Color(0xfffcca05)
    val clearnessColor = Color(0xff63d1d2)

    RoundedCornerBox(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.stats_dreams_metric_title),
                style = titleStyle
            )
            Spacer(
                Modifier.height(12.dp)
            )

            when (state) {
                DreamMetricState.Loading -> TODO()
                DreamMetricState.NoDate -> TODO()
                is DreamMetricState.Success -> {
                    Column {
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
                                    decimalFormat.format(state.happinessAverage)
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
                                    decimalFormat.format(state.clearnessAverage)
                                ),
                                style = labelStyle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DreamMetricComponentPreview() = DreamMetricComponent(
    modifier = Modifier
        .width(350.dp),
    state = DreamMetricState.Success(
        happinessAverage = 0.1809528F,
        clearnessAverage = 0.9186238F
    )
)
