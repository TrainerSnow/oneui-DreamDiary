package com.snow.diary.feature.relations.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.feature.relations.R
import com.snow.diary.core.ui.feed.RelationFeed
import com.snow.diary.core.ui.feed.RelationFeedState
import com.snow.diary.core.ui.util.SortSection
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun RelationList(
    viewModel: RelationListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAddRelation: () -> Unit,
    onSearchRelation: () -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    val sortConfig by viewModel.sortConfig.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()

    RelationList(
        state = feedState,
        sortConfig = sortConfig,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onAddClick = onAddRelation,
        onSearchClick = onSearchRelation,
        onRelationClick = onRelationClick
    )
}

@Composable
private fun RelationList(
    state: RelationFeedState,
    sortConfig: SortConfig,
    onEvent: (RelationsListEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(R.string.relation_list_title),
        toolbarSubtitle = (state as? RelationFeedState.Success)?.let {
            stringResource(
                R.string.relation_list_subtitle,
                state.relations.size.toString()
            )
        },
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_back),
                onClick = onNavigateBack
            )
        },
        appbarActions = {
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_add
                ),
                onClick = onAddClick
            )
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_search
                ),
                onClick = onSearchClick
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RelationListScreenDefaults.sortSectionPadding),
            horizontalArrangement = Arrangement.End
        ) {
            SortSection(
                sortConfig = sortConfig,
                onSortChange = {
                    onEvent(RelationsListEvent.SortChange(it))
                }
            )
        }
        RelationFeed(
            modifier = Modifier
                .fillMaxWidth(),
            state = state,
            onRelationClick = onRelationClick
        )
    }
}

private object RelationListScreenDefaults {

    val sortSectionPadding = PaddingValues(
        horizontal = 12.dp
    )

}