package com.snow.diary.feature.statistics.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.feature.statistics.R
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox


//Loading and NoData need to be classes so we can make them inherit the <Data> generic type.
internal sealed class StatisticsState<Data> {

    class Loading<Data> : StatisticsState<Data>()

    class NoData<Data> : StatisticsState<Data>()

    data class Success<Data>(
        val data: Data
    ) : StatisticsState<Data>()

    companion object {

        fun <Data> from(data: Data): StatisticsState<Data> = StatisticsState.Success(data)

    }

}

@Composable
internal fun <Data> StatisticsComponent(
    modifier: Modifier = Modifier,
    title: String,
    state: StatisticsState<Data>,
    onClick: (() -> Unit)? = null,
    content: @Composable (Data) -> Unit
) = StatisticsComponent(
    modifier = modifier,
    title = { Text(title) },
    state = state,
    onClick = onClick,
    content = content,
)

@Composable
internal fun <Data> StatisticsComponent(
    modifier: Modifier = Modifier,
    title: @Composable (StatisticsState<Data>) -> Unit,
    state: StatisticsState<Data>,
    onClick: (() -> Unit)? = null,
    content: @Composable (Data) -> Unit
) {
    RoundedCornerBox(
        modifier = modifier,
        onClick = onClick
    ) {
        val titleStyle = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold,
            color = OneUITheme.colors.seslPrimaryTextColor
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ProvideTextStyle(titleStyle) {
                title(state)
            }

            when (state) {
                is StatisticsState.Loading -> {
                    Loading(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                is StatisticsState.NoData -> {
                    NoData(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                is StatisticsState.Success -> {
                    content(state.data)
                }
            }
        }
    }
}

@Composable
private fun NoData(
    modifier: Modifier = Modifier
) {
    val infoStyle = TextStyle(
        fontSize = 13.sp,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    Box(
        modifier = modifier
            .height(StatisticsComponentDefaults.infoBoxHeight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = infoStyle,
            text = stringResource(R.string.stats_dreams_no_data_available)
        )
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    val infoStyle = TextStyle(
        fontSize = 13.sp,
        color = OneUITheme.colors.seslPrimaryTextColor
    )
    Column(
        modifier = modifier
            .height(StatisticsComponentDefaults.infoBoxHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(12.dp)
    ) {
        ProgressIndicator(
            type = ProgressIndicatorType.CircularIndeterminate(
                size = CircularProgressIndicatorSize.Companion.Medium
            )
        )
        Text(
            style = infoStyle,
            text = stringResource(R.string.stats_dreams_no_data_available)
        )
    }
}

private object StatisticsComponentDefaults {

    val infoBoxHeight = 150.dp

}