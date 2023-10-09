package com.snow.diary.core.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.ui.R
import org.oneui.compose.dialog.AlertDialog
import org.oneui.compose.widgets.buttons.radio.ListRadioButton
import org.oneui.compose.widgets.buttons.radio.VerticalRadioGroup
import java.time.temporal.ChronoUnit

enum class StatisticsDateRanges(
    val range: DateRange,
    @StringRes val displayName: Int
) {

    AllTime(
        DateRange.AllTime,
        R.string.range_alltime
    ),

    RecentMonth(
        DateRange.LastN(
            n = 1,
            unit = ChronoUnit.MONTHS
        ),
        R.string.range_month
    ),

    RecentHalfYear(
        DateRange.LastN(
            n = 6,
            unit = ChronoUnit.MONTHS
        ),
        R.string.range_halfyear
    ),

    RecentYear(
        DateRange.LastN(
            n = 1,
            unit = ChronoUnit.YEARS
        ),
        R.string.range_year
    ),

    RecentFiveYears(
        DateRange.LastN(
            n = 5,
            unit = ChronoUnit.YEARS
        ),
        R.string.range_fiveyear
    )

}

@Composable
fun DateRangeDialog(
    onDismissRequest: () -> Unit,
    selected: StatisticsDateRanges,
    onRangeSelect: (StatisticsDateRanges) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.range_dialog_title)
            )
        },
        negativeButtonLabel = stringResource(org.oneui.compose.R.string.sesl_picker_cancel),
        onNegativeButtonClick = onDismissRequest,
        body = {
            VerticalRadioGroup(
                spacing = 0.dp
            ) {
                StatisticsDateRanges.entries.forEach {
                    ListRadioButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = it,
                        groupValue = selected,
                        onClick = { onRangeSelect(it); onDismissRequest() },
                        label = { Text(text = stringResource(it.displayName)) }
                    )
                }
            }
        }
    )
}