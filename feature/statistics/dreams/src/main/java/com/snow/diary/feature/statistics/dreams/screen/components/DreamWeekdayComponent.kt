package com.snow.diary.feature.statistics.dreams.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.time.TimeFormat.formatFullName
import com.snow.diary.feature.statistics.dreams.R
import org.oneui.compose.preference.misc.PreferenceListDivider
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox
import java.time.DayOfWeek

internal sealed class DreamWeekdayState {

    data object Loading : DreamWeekdayState()

    data object NoData : DreamWeekdayState()

    data class Success(
        val weekdays: List<DreamWeekdayInformation>, val mostDreamsOn: DayOfWeek
    ) : DreamWeekdayState()

}

internal data class DreamWeekdayInformation(

    val weekday: DayOfWeek,

    val totalAmount: Int,

    val percentage: Float

)

@Composable
internal fun DreamWeekday(
    modifier: Modifier = Modifier, state: DreamWeekdayState
) {
    val titleStyle = TextStyle(
        fontSize = 21.sp,
        fontWeight = FontWeight.SemiBold,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val mostDreamsStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val listDayStyle = TextStyle(
        fontSize = 19.sp,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val listPercentageStyle = TextStyle(
        fontSize = 19.sp, fontWeight = FontWeight.SemiBold,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    val errorTextStyle = TextStyle(
        fontSize = 13.sp,
        color = OneUITheme.colors.seslPrimaryTextColor
    )

    RoundedCornerBox(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.stats_dreams_weekday_title), style = titleStyle
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (state) {
                    DreamWeekdayState.Loading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement
                                .spacedBy(8.dp)
                        ) {
                            ProgressIndicator(
                                type = ProgressIndicatorType.CircularIndeterminate(
                                    size = CircularProgressIndicatorSize.Companion.Medium
                                )
                            )
                            Text(
                                text = stringResource(R.string.stats_dreams_loading),
                                style = errorTextStyle
                            )
                        }
                    }

                    DreamWeekdayState.NoData -> {
                        Text(
                            text = stringResource(R.string.stats_dreams_no_data_available),
                            style = errorTextStyle
                        )
                    }

                    is DreamWeekdayState.Success -> {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(
                                R.string.stats_dreams_weekday_most,
                                state.mostDreamsOn.formatFullName()
                            ),
                            style = mostDreamsStyle
                        )

                        PreferenceListDivider(
                            Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )

                        state.weekdays.sortedByDescending { it.percentage }
                            .take(DreamWeekdayDefaults.showTopNWeekdays).forEach { info ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = info.weekday.formatFullName() + ":",
                                        style = listDayStyle
                                    )
                                    Text(
                                        text = (info.percentage * 100).toInt().toString() + "%",
                                        style = listPercentageStyle
                                    )
                                }
                            }
                    }
                }
            }
        }
    }
}

private object DreamWeekdayDefaults {

    const val showTopNWeekdays = 3

}

@Preview
@Composable
private fun DreamWeekdayPreviewSuccess() {
    DreamWeekday(
        state = DreamWeekdayState.Success(
            weekdays = listOf(
                DreamWeekdayInformation(
                    weekday = DayOfWeek.MONDAY, totalAmount = 74, percentage = 1 / 11F
                ), DreamWeekdayInformation(
                    weekday = DayOfWeek.TUESDAY, totalAmount = 120, percentage = 1 / 6F
                ), DreamWeekdayInformation(
                    weekday = DayOfWeek.WEDNESDAY, totalAmount = 23, percentage = 1 / 15F
                ), DreamWeekdayInformation(
                    weekday = DayOfWeek.THURSDAY, totalAmount = 78, percentage = 1 / 12F
                ), DreamWeekdayInformation(
                    weekday = DayOfWeek.FRIDAY, totalAmount = 12, percentage = 1 / 24F
                ), DreamWeekdayInformation(
                    weekday = DayOfWeek.SATURDAY, totalAmount = 274, percentage = 1 / 3F
                ), DreamWeekdayInformation(
                    weekday = DayOfWeek.SUNDAY, totalAmount = 213, percentage = 1 / 4F
                )
            ), mostDreamsOn = DayOfWeek.SATURDAY
        )
    )
}

@Preview
@Composable
private fun DreamWeekdayPreviewLoading() {
    DreamWeekday(
        state = DreamWeekdayState.Loading
    )
}

@Preview
@Composable
private fun DreamWeekdayPreviewNoData() {
    DreamWeekday(
        state = DreamWeekdayState.NoData
    )
}




































