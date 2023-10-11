package com.snow.diary.core.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.R
import com.snow.diary.core.ui.callback.DreamCallback
import com.snow.diary.core.ui.data.DreamPreviewData
import com.snow.diary.core.ui.item.DreamCard
import com.snow.diary.core.ui.screen.EmptyScreen
import com.snow.diary.core.ui.screen.ErrorScreen
import com.snow.diary.core.ui.screen.LoadingScreen
import com.snow.diary.core.ui.util.windowSizeClass
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.text.TextSeparator
import java.time.LocalDate
import java.time.temporal.ChronoUnit

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

        is DreamFeedState.Success -> {
            SuccessFeed(
                state = state,
                dreamCallback = dreamCallback
            )
        }
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: DreamFeedState.Success,
    dreamCallback: DreamCallback
) {
    val doListPositions = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val ranges = listOf(
        DateRange.LastN(
            0,
            ChronoUnit.DAYS
        ) to stringResource(R.string.dreamfeed_tempsort_today),
        DateRange.LastN(
            1,
            ChronoUnit.DAYS
        ) to stringResource(R.string.dreamfeed_tempsort_yesterday),
        DateRange.LastN(
            1,
            ChronoUnit.WEEKS
        ) to stringResource(R.string.dreamfeed_tempsort_thisweek),
        DateRange.LastN(
            1,
            ChronoUnit.MONTHS
        ) to stringResource(R.string.dreamfeed_tempsort_thismonth),
        DateRange.AllTime to stringResource(R.string.dreamfeed_tempsort_earlier)
    )

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = DreamFeedDefaults.dreamItemMinSize
        ),
        horizontalArrangement = if (doListPositions) Arrangement.Start
        else Arrangement.spacedBy(4.dp),
        verticalArrangement = if (doListPositions) Arrangement.Top else Arrangement.spacedBy(4.dp)
    ) {
        val doTemporallySort =
            state.temporallySort && state.sortConfig.mode.let { it == SortMode.Created || it == SortMode.Updated }

        if (!doTemporallySort) {
            items(
                count = state.dreams.size,
                key = { state.dreams[it].id ?: 0L },
                contentType = { DreamFeedDefaults.ctypeDreamitem }
            ) {
                DreamCard(
                    dream = state.dreams[it],
                    listPosition = if (doListPositions) ListPosition.get(
                        state.dreams[it],
                        state.dreams
                    ) else ListPosition.Single,
                    dreamCallback = dreamCallback
                )
            }
        } else {
            val datePicker: (Dream) -> LocalDate =
                if (state.sortConfig.mode == SortMode.Created) Dream::created else Dream::updated

            temporallySortedDreams(
                dreams = state.dreams,
                getDate = datePicker,
                ranges = ranges,
                doListPositions = doListPositions,
                callback = dreamCallback
            )
        }
    }
}

private fun LazyGridScope.temporallySortedDreams(
    dreams: List<Dream>, //We assume is sorted descending
    getDate: (Dream) -> LocalDate,
    ranges: List<Pair<DateRange, String>>, //We assume is sorted
    doListPositions: Boolean,
    callback: DreamCallback
) {
    var currentIndex = 0
    var firstOfRange = true

    dreams.forEachIndexed { dreamIndex, dream ->
        val date = getDate(dream)
        val lastDate = dreams.getOrNull(dreamIndex - 1)?.let { getDate(it) }
        val nextDate = dreams.getOrNull(dreamIndex + 1)?.let { getDate(it) }

        val lastRangeIndex = currentIndex

        while (!ranges[currentIndex].first.contains(date)) {
            currentIndex += 1
            firstOfRange = true
        }

        val rangeIndexChanged = lastRangeIndex != currentIndex

        val isNextDateInRange = nextDate?.let { ranges[currentIndex].first.contains(it) } ?: false
        val wasLastDateInRange =
            lastDate?.let { ranges[currentIndex].first.contains(lastDate) } ?: false

        if (firstOfRange) {
            separatorItem(ranges[currentIndex].second)
            firstOfRange = false
        }

        val listPos = if (!doListPositions) ListPosition.Single
        else when (isNextDateInRange) {
            true -> when (wasLastDateInRange && !rangeIndexChanged) {
                true -> ListPosition.Middle
                false -> ListPosition.First
            }

            false -> when (wasLastDateInRange && !rangeIndexChanged) {
                true -> ListPosition.Last
                false -> ListPosition.Single
            }
        }

        item {
            DreamCard(
                dream = dreams[dreamIndex],
                listPosition = listPos,
                dreamCallback = callback
            )
        }
    }
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
     * [temporallySort] should only be true when [dreams] are sorted by [Dream.created] or [Dream.updated], [SortDirection.Descending]. Otherwise, an [IllegalStateException] is thrown.
     */
    data class Success(
        val dreams: List<Dream>,
        val temporallySort: Boolean,
        val sortConfig: SortConfig
    ) : DreamFeedState() {

        init {
            if (temporallySort) {
                require(
                    (sortConfig.mode == SortMode.Created || sortConfig.mode == SortMode.Updated) &&
                            sortConfig.direction == SortDirection.Descending
                )
            }
        }

    }

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
            mode = SortMode.Created,
            direction = SortDirection.Descending
        )
    )
)
