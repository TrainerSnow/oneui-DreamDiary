package com.snow.diary.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.common.util.ListPosition
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.R
import com.snow.diary.ui.dream.DreamCard
import com.snow.diary.ui.data.DreamPreviewData
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.base.iconColors
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.text.TextSeparator
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import dev.oneuiproject.oneui.R as IconR

@Composable
fun DreamFeed(
    modifier: Modifier = Modifier,
    state: DreamFeedState,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit
) {
    when (state) {
        DreamFeedState.Empty -> {
            EmptyFeed(modifier)
        }

        is DreamFeedState.Error -> {
            ErrorFeed(modifier, state)
        }

        DreamFeedState.Loading -> {
            LoadingFeed(modifier)
        }

        is DreamFeedState.Success -> SuccessFeed(
            state = state,
            onDreamClick = onDreamClick,
            onDreamFavouriteClick = onDreamFavouriteClick
        )
    }
}

@Composable
private fun EmptyFeed(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(DreamFeedDefaults.emptyIconTextSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val style = TextStyle(
            color = OneUITheme.colors.seslSecondaryTextColor,
            fontSize = 12.sp
        )

        IconView(
            icon = Icon.Resource(IconR.drawable.ic_oui_lasso_add),
            colors = iconColors(
                tint = OneUITheme.colors.seslSecondaryTextColor.copy(
                    alpha = 0.4F
                )
            ),
            modifier = Modifier
                .size(DreamFeedDefaults.emptyIconSize),
            contentDescription = stringResource(
                id = R.string.dreamfeed_empty_label
            )
        )
        Text(
            text = stringResource(
                id = R.string.dreamfeed_empty_label
            ),
            style = style
        )
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: DreamFeedState.Success,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit
) {

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val weekBegin = today.with(DayOfWeek.MONDAY)
    val monthBegin = today.with(TemporalAdjusters.firstDayOfMonth())
    val ever = LocalDate.MIN

    val ranges = listOf(
        (today to today) to stringResource(R.string.dreamfeed_tempsort_today),
        (yesterday to today) to stringResource(R.string.dreamfeed_tempsort_yesterday),
        (weekBegin to yesterday) to stringResource(R.string.dreamfeed_tempsort_thisweek),
        (monthBegin to weekBegin) to stringResource(R.string.dreamfeed_tempsort_thismonth),
        (ever to monthBegin) to stringResource(R.string.dreamfeed_tempsort_earlier),
    )
    val datePicker: (Dream) -> LocalDate = when (state.sortConfig.mode) {
        SortMode.Created -> {
            {
                it.created
            }
        }

        else -> {
            {
                it.updated
            }
        }
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = DreamFeedDefaults.dreamItemMinSize
        )
    ) {
        if (!state.temporallySort) {
            items(
                count = state.dreams.size,
                key = { state.dreams[it].id },
                contentType = { DreamFeedDefaults.ctypeDreamitem }
            ) {
                DreamCard(
                    dream = state.dreams[it],
                    listPosition = ListPosition.get(state.dreams[it], state.dreams),
                    onClick = { onDreamClick(state.dreams[it]) },
                    onFavouriteClick = { onDreamFavouriteClick(state.dreams[it]) }
                )
            }
            return@LazyVerticalGrid
        }

        require(state.sortConfig.mode == SortMode.Created || state.sortConfig.mode == SortMode.Updated) { "temporallySort is enabled, but the SortConfig is not sorting by date." }

        var fromIndex = 0
        ranges.forEach {
            val title = it.second
            val from = it.first.first
            val to = it.first.second

            val res = temporallyDreamItems(
                dreams = state.dreams.subList(
                    fromIndex, state.dreams.size
                ),
                date = datePicker,
                onDreamClick = onDreamClick,
                onDreamFavouriteClick = onDreamFavouriteClick,
                separatorTitle = title,
                dateFrom = from,
                dateTo = to
            )

            fromIndex += res + 1
        }
    }
}

private fun LazyGridScope.temporallyDreamItems(
    dreams: List<Dream>,
    date: (Dream) -> LocalDate,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit,
    separatorTitle: String,
    dateFrom: LocalDate, //Inclusive
    dateTo: LocalDate //Exclusive
): Int {
    val drms = dreams.takeWhile {
        date(it).isEqual(dateFrom) || date(it).isAfter(dateFrom) &&
                date(it).isBefore(dateTo)
    }

    if (drms.isNotEmpty()) {
        separatorItem(separatorTitle)
        items(
            count = drms.size,
            key = { drms[it].id },
            contentType = { DreamFeedDefaults.ctypeDreamitem }
        ) {
            DreamCard(
                dream = drms[it],
                onClick = { onDreamClick(drms[it]) },
                onFavouriteClick = { onDreamFavouriteClick(drms[it]) },
                listPosition = ListPosition.get(drms[it], drms)
            )
        }

        return dreams.indexOf(drms.last())
    }

    return -1
}

private fun LazyGridScope.separatorItem(
    title: String
) {
    item(
        key = title,
        span = { GridItemSpan(maxLineSpan) },
        contentType = DreamFeedDefaults.ctypeSeparator
    ) {
        TextSeparator(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ErrorFeed(
    modifier: Modifier = Modifier,
    state: DreamFeedState.Error
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val warningStyle = TextStyle(
            color = OneUITheme.colors.seslErrorColor,
            fontSize = 12.sp
        )

        IconView(
            icon = Icon.Resource(IconR.drawable.ic_oui_error),
            colors = iconColors(
                tint = OneUITheme.colors.seslErrorColor
            ),
            modifier = Modifier
                .size(DreamFeedDefaults.errorIconSize),
            contentDescription = stringResource(
                id = R.string.dreamfeed_empty_label
            )
        )
        Spacer(
            modifier = Modifier
                .height(DreamFeedDefaults.errorIconTextSpacing)
        )
        Text(
            text = stringResource(
                id = R.string.dreamfeed_error_label
            ),
            style = warningStyle
        )
        Spacer(
            modifier = Modifier
                .height(DreamFeedDefaults.errorTextMsgSPacing)
        )
        Text(
            text = state.msg,
            style = warningStyle
        )
    }
}

@Composable
private fun LoadingFeed(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(DreamFeedDefaults.loadingIconTextSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val style = TextStyle(
            color = OneUITheme.colors.seslPrimaryTextColor,
            fontSize = 12.sp
        )

        ProgressIndicator(
            type = ProgressIndicatorType.CircularIndeterminate(
                size = CircularProgressIndicatorSize.Companion.Large
            )
        )
        Text(
            text = stringResource(
                id = R.string.dreamfeed_loading_label
            ),
            style = style
        )
    }
}

private object DreamFeedDefaults {

    val emptyIconSize = 65.dp
    val emptyIconTextSpacing = 16.dp

    val loadingIconTextSpacing = 16.dp

    val errorIconSize = 65.dp
    val errorIconTextSpacing = 16.dp
    val errorTextMsgSPacing = 4.dp

    val dreamItemMinSize = 350.dp

    const val ctypeSeparator = "separator"
    const val ctypeDreamitem = "dreamitem"

}

sealed class DreamFeedState {

    data object Empty : DreamFeedState()

    data class Error(
        val msg: String
    ) : DreamFeedState()

    data object Loading : DreamFeedState()

    /**
     * Dreams have been retrieved.
     *
     * [temporallySort] should only be true when [dreams] are sorted by [Dream.created] or [Dream.updated]. Otherwise, an [IllegalStateException] is thrown.
     */
    data class Success(
        val dreams: List<Dream>,
        val temporallySort: Boolean,
        val sortConfig: SortConfig
    ) : DreamFeedState()

}

@Preview
@Composable
private fun DreamFeedEmpty(

) = OneUIPreview(title = "DreamFeedEmpty", padding = PaddingValues()) {
    DreamFeed(
        state = DreamFeedState.Empty,
        onDreamClick = { },
        onDreamFavouriteClick = { }
    )
}

@Preview
@Composable
private fun DreamFeedError(

) = OneUIPreview(title = "DreamFeedError", padding = PaddingValues()) {
    DreamFeed(
        state = DreamFeedState.Error(
            msg = "ErrorCode[404]"
        ),
        onDreamClick = { },
        onDreamFavouriteClick = { }
    )
}

@Preview
@Composable
private fun DreamFeedLoading(

) = OneUIPreview(title = "DreamFeedLoading", padding = PaddingValues()) {
    DreamFeed(
        state = DreamFeedState.Loading,
        onDreamClick = { },
        onDreamFavouriteClick = { }
    )
}

@Preview
@Composable
private fun DreamFeedSuccess() = OneUIPreview(title = "DreamFeedSuccess", padding = PaddingValues()) {
    DreamFeed(
        state = DreamFeedState.Success(
            dreams = DreamPreviewData.dreams,
            temporallySort = false,
            sortConfig = SortConfig(
                mode = SortMode.Updated
            )
        ),
        onDreamClick = { },
        onDreamFavouriteClick = { }
    )
}
