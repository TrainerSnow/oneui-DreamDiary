package com.snow.diary.feature.statistics.screen.dream.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

data class DreamAmountData(

    val amount: Int,

    val average: Float

)

@Composable
internal fun DreamAmount(
    modifier: Modifier = Modifier,
    state: StatisticsState<DreamAmountData>,
    onClick: (() -> Unit)? = null
) {
    val amountStyle = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.SemiBold,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val averageStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val decimalFormat = DecimalFormat("#.##")

    StatisticsComponent(
        modifier = modifier,
        title = stringResource(R.string.stats_dreams_amount_title),
        state = state,
        onClick = onClick,
    ) { data ->
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement
                .spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.stats_dreams_amount_amount,
                    data.amount.toString()
                ),
                style = amountStyle
            )

            Text(
                text = stringResource(
                    R.string.stats_dreams_amount_avg,
                    decimalFormat.format(data.average)
                ),
                style = averageStyle
            )
        }
    }
}

@Preview
@Composable
private fun DreamAmountPreview() = DreamAmount(
    modifier = Modifier
        .width(360.dp),
    state = StatisticsState.Success(
        DreamAmountData(
            amount = 278,
            average = 0.25381932F
        )
    )
)