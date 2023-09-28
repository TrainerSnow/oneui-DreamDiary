package com.snow.diary.feature.statistics.screen.locations.components

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
import com.snow.diary.core.model.data.Coordinates
import com.snow.diary.core.model.data.Location
import com.snow.diary.feature.statistics.R
import com.snow.diary.feature.statistics.screen.components.StatisticsComponent
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import org.oneui.compose.preference.misc.PreferenceListDivider
import org.oneui.compose.theme.OneUITheme

data class LocationsAmountData(

    //All locations
    val locationsNum: Int,

    //Total locations used in dreams
    val usedLocationsNum: Int,

    val most: List<Pair<Location, Int>>

)

@Composable
internal fun LocationsAmount(
    modifier: Modifier = Modifier,
    state: StatisticsState<LocationsAmountData>
) {
    StatisticsComponent(
        modifier = modifier,
        title = stringResource(R.string.stats_locations_amount_title),
        state = state
    ) { data ->
        require(data.most.size >= 2) { "Data#most must be at least 3 long" }
        val numberStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = OneUITheme.colors.seslPrimaryColor
        )
        val labelStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = OneUITheme.colors.seslPrimaryTextColor
        )
        val locationNumberStyle = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            color = OneUITheme.colors.seslSecondaryTextColor
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement
                .spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(4.dp)
            ) {
                Text(
                    style = labelStyle,
                    text = stringResource(R.string.stats_locations_amount_total)
                )
                Text(
                    style = numberStyle,
                    text = data.locationsNum.toString()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(4.dp)
            ) {
                Text(
                    style = labelStyle,
                    text = stringResource(R.string.stats_locations_amount_used)
                )
                Text(
                    style = numberStyle,
                    text = data.usedLocationsNum.toString()
                )
            }

            PreferenceListDivider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Column(
                verticalArrangement = Arrangement
                    .spacedBy(8.dp)
            ) {
                data.most.forEach { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pair.first.name,
                            style = labelStyle
                        )
                        Text(
                            text = stringResource(
                                id = R.string.stats_locations_amount_most_amount,
                                pair.second.toString()
                            ),
                            style = locationNumberStyle
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LocationsAMountPreview() {
    LocationsAmount(
        state = StatisticsState.from(
            LocationsAmountData(
                locationsNum = 55,
                usedLocationsNum = 429,
                most = listOf(
                    Location(1L, "Location 1", Coordinates(1F, 1F), "blah") to 42,
                    Location(2L, "Location 2", Coordinates(1F, 1F), "blah") to 33,
                    Location(3L, "Location 3", Coordinates(1F, 1F), "blah") to 29,
                )
            )
        )
    )
}