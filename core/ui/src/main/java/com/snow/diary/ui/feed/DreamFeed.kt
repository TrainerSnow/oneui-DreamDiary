package com.snow.diary.ui.feed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.R
import com.snow.diary.ui.callback.DreamCallback
import com.snow.diary.ui.data.DreamPreviewData
import com.snow.diary.ui.item.DreamCard
import com.snow.diary.ui.screen.EmptyScreen
import com.snow.diary.ui.screen.ErrorScreen
import com.snow.diary.ui.screen.LoadingScreen
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.text.TextSeparator
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun DreamFeed(
    modifier: Modifier = Modifier,
    state: DreamFeedState,
    dreamCallback: DreamCallback = DreamCallback
) {
    when (state) {
        DreamFeedState.Empty -> {
            EmptyScreen(
                modifier,
                title = stringResource(
                    id = R.string.dreamfeed_empty_label
                )
            )
        }

        is DreamFeedState.Error -> {
            ErrorScreen(
                modifier = modifier,
                title = stringResource(
                    id = R.string.dreamfeed_error_label
                ),
                description = state.msg
            )
        }

        DreamFeedState.Loading -> {
            LoadingScreen(
                title = stringResource(
                    id = R.string.dreamfeed_loading_label
                )
            )
        }

        is DreamFeedState.Success -> SuccessFeed(
            state = state,
            dreamCallback = dreamCallback
        )
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: DreamFeedState.Success,
    dreamCallback: DreamCallback
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
        val doTemporallySort = state.temporallySort && state.sortConfig.mode.let { it == SortMode.Created || it == SortMode.Updated }

        if (!doTemporallySort) {
            items(
                count = state.dreams.size,
                key = { state.dreams[it].id ?: 0L },
                contentType = { DreamFeedDefaults.ctypeDreamitem }
            ) {
                DreamCard(
                    dream = state.dreams[it],
                    listPosition = ListPosition.get(state.dreams[it], state.dreams),
                    dreamCallback = dreamCallback
                )
            }
            return@LazyVerticalGrid
        }

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
                dreamCallback = dreamCallback,
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
    dreamCallback: DreamCallback,
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
            key = { drms[it].id ?: 0L },
            contentType = { DreamFeedDefaults.ctypeDreamitem }
        ) {
            DreamCard(
                dream = drms[it],
                dreamCallback = dreamCallback,
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

private object DreamFeedDefaults {

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

) = OneUIPreview(title = "DreamFeedEmpty") {
    DreamFeed(
        state = DreamFeedState.Empty
    )
}

@Preview
@Composable
private fun DreamFeedError(

) = OneUIPreview(title = "DreamFeedError") {
    DreamFeed(
        state = DreamFeedState.Error(
            msg = "ErrorCode[404]"
        )
    )
}

@Preview
@Composable
private fun DreamFeedLoading(

) = OneUIPreview(title = "DreamFeedLoading") {
    DreamFeed(
        state = DreamFeedState.Loading
    )
}

@Preview
@Composable
private fun DreamFeedSuccess() = DreamFeed(
    state = DreamFeedState.Success(
        dreams = DreamPreviewData.dreams,
        temporallySort = true,
        sortConfig = SortConfig(
            mode = SortMode.Created
        )
    )
)
