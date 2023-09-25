package com.snow.diary.feature.statistics.screen.dream.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.time.TimeFormat.formatFullName
import com.snow.diary.feature.statistics.R
import com.snow.diary.feature.statistics.screen.components.StatisticsComponent
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import org.oneui.compose.preference.misc.PreferenceListDivider
import org.oneui.compose.theme.OneUITheme
import java.time.DayOfWeek

internal data class DreamWeekdayData(

    val weekdays: List<DreamWeekdayInformation>,

    val mostDreamsOn: DayOfWeek

)

internal data class DreamWeekdayInformation(

    val weekday: DayOfWeek,

    val totalAmount: Int,

    val percentage: Float

)

@Composable
internal fun DreamWeekday(
    modifier: Modifier = Modifier,
    state: StatisticsState<DreamWeekdayData>
) {
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

    StatisticsComponent(
        modifier = modifier,
        title = stringResource(R.string.stats_dreams_weekday_title),
        state = state
    ) { data ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(
                    R.string.stats_dreams_weekday_most,
                    data.mostDreamsOn.formatFullName()
                ),
                style = mostDreamsStyle
            )

            PreferenceListDivider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.CenterHorizontally)
            )

            data.weekdays.sortedByDescending { it.percentage }
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

private object DreamWeekdayDefaults {

    const val showTopNWeekdays = 3

}




































