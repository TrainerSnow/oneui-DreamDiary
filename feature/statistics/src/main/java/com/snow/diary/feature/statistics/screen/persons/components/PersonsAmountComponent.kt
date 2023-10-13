package com.snow.diary.feature.statistics.screen.persons.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.snow.diary.core.model.data.Person
import com.snow.diary.feature.statistics.R
import com.snow.diary.feature.statistics.screen.components.StatisticsComponent
import com.snow.diary.feature.statistics.screen.components.StatisticsState
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.Divider

data class PersonsAmountData(

    //All persons
    val personsNum: Int,

    //Total persons used in dreams
    val usedPersonsNum: Int,

    val most: List<Pair<Person, Int>>

)

@Composable
internal fun PersonsAmount(
    modifier: Modifier = Modifier,
    state: StatisticsState<PersonsAmountData>
) {
    StatisticsComponent(
        modifier = modifier,
        title = stringResource(R.string.stats_persons_amount_title),
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
            fontWeight = FontWeight.SemiBold
        )
        val personNumberStyle = TextStyle(
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
                    text = stringResource(R.string.stats_persons_amount_total)
                )
                Text(
                    style = numberStyle,
                    text = data.personsNum.toString()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(4.dp)
            ) {
                Text(
                    style = labelStyle,
                    text = stringResource(R.string.stats_persons_amount_used)
                )
                Text(
                    style = numberStyle,
                    text = data.usedPersonsNum.toString()
                )
            }

            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                padding = PaddingValues()
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
                                id = R.string.stats_persons_amount_most_amount,
                                pair.second.toString()
                            ),
                            style = personNumberStyle
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PersonsAMountPreview() {
    PersonsAmount(
        state = StatisticsState.from(
            PersonsAmountData(
                personsNum = 55,
                usedPersonsNum = 429,
                most = listOf(
                    Person(1L, "Person 1", true, "blah") to 42,
                    Person(2L, "Person 2", false, "blah") to 33,
                    Person(3L, "Person 3", false, "blah") to 29,
                )
            )
        )
    )
}