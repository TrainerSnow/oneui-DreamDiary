package com.snow.diary.core.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.ui.R
import com.snow.diary.core.ui.item.RelationCard
import com.snow.diary.core.ui.screen.EmptyScreen
import com.snow.diary.core.ui.screen.ErrorScreen
import com.snow.diary.core.ui.screen.LoadingScreen
import com.snow.diary.core.ui.util.windowSizeClass
import org.oneui.compose.util.ListPosition


@Composable
fun RelationFeed(
    modifier: Modifier = Modifier,
    state: RelationFeedState,
    onRelationClick: (Relation) -> Unit
) {
    when (state) {
        RelationFeedState.Empty -> {
            EmptyScreen(
                modifier,
                title = stringResource(
                    id = R.string.relationfeed_empty_label
                )
            )
        }

        is RelationFeedState.Error -> {
            ErrorScreen(
                modifier = modifier,
                title = stringResource(
                    id = R.string.relationfeed_error_label
                ),
                description = state.msg
            )
        }

        RelationFeedState.Loading -> {
            LoadingScreen(
                title = stringResource(
                    id = R.string.relationfeed_error_label
                )
            )
        }

        is RelationFeedState.Success -> {
            SuccessFeed(
                state = state,
                onRelationClick = onRelationClick
            )
        }
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: RelationFeedState.Success,
    onRelationClick: (Relation) -> Unit
) {
    val doListPositions = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = RelationFeedDefaults.relationItemMinWidth
        ),
        horizontalArrangement = if (doListPositions) Arrangement.Start
        else Arrangement.spacedBy(4.dp),
        verticalArrangement = if (doListPositions) Arrangement.Top else Arrangement.spacedBy(4.dp)
    ) {
        items(
            count = state.relations.size,
            key = { state.relations[it].id ?: 0L },
            contentType = { RelationFeedDefaults.ctypeRelationItem }
        ) {
            val pos = if (doListPositions) ListPosition.get(state.relations[it], state.relations)
            else ListPosition.Single

            RelationCard(
                relation = state.relations[it],
                listPosition = pos,
                onClick = onRelationClick
            )
        }
    }
}

private object RelationFeedDefaults {

    val relationItemMinWidth = 350.dp

    const val ctypeRelationItem = "relationItem"

}

sealed class RelationFeedState {

    data object Empty : RelationFeedState()

    data class Error(
        val msg: String
    ) : RelationFeedState()

    data object Loading : RelationFeedState()

    /**
     * Persons have been retrieved.
     */
    data class Success(
        val relations: List<Relation>
    ) : RelationFeedState()

}