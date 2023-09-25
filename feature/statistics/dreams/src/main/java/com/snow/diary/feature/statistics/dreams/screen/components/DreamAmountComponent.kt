package com.snow.diary.feature.statistics.dreams.screen.components

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
import com.snow.diary.feature.statistics.dreams.R
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox

internal sealed class DreamAmountState {

    data class Success(
        val amount: Int,
        val monthlyAverage: Float
    ) : DreamAmountState()

    data object Loading : DreamAmountState()

}

@Composable
internal fun DreamAmount(
    modifier: Modifier = Modifier,
    state: DreamAmountState,
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

    RoundedCornerBox(
        modifier = modifier,
        onClick = onClick
    ) {
        when (state) {
            DreamAmountState.Loading -> {
                ProgressIndicator(
                    type = ProgressIndicatorType.CircularIndeterminate(
                        size = CircularProgressIndicatorSize.Companion.Medium
                    )
                )
            }
            is DreamAmountState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement
                        .spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.stats_dreams_amount_amount,
                            state.amount.toString()
                        ),
                        style = amountStyle
                    )

                    Text(
                        text = stringResource(
                            R.string.stats_dreams_amount_avg,
                            decimalFormat.format(state.monthlyAverage)
                        ),
                        style = averageStyle
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DreamAmountPreview() = DreamAmount(
    modifier = Modifier
        .width(360.dp),
    state = DreamAmountState.Success(
        amount = 278,
        monthlyAverage = 0.25381932F
    )
)