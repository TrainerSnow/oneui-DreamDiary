package com.snow.feature.dreams.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortDirection
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.callback.DreamCallback
import com.snow.diary.ui.data.DreamPreviewData
import com.snow.diary.ui.feed.DreamFeed
import com.snow.diary.ui.feed.DreamFeedState
import com.snow.diary.ui.util.SortSection
import com.snow.feature.dreams.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.menu.MenuItem
import org.oneui.compose.widgets.menu.PopupMenu
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun DreamListScreen(
    viewModel: DreamListViewModel = hiltViewModel()
) {

    val listState by viewModel.dreamListState.collectAsStateWithLifecycle()
    val sortConfig by viewModel.sortConfig.collectAsStateWithLifecycle()
    val showMenu by viewModel.showMenu.collectAsStateWithLifecycle()

    DreamListScreen(
        listState = listState,
        sortConfig = sortConfig,
        showMenu = showMenu,
        onAddClick = { /*TODO*/ },
        onSearchClick = { /*TODO*/ },
        onMenuClick = viewModel::onMenuClick,
        onDreamClick = { /*TODO*/ },
        onDreamFavouriteClick = viewModel::onDreamFavouriteClick,
        onSortChange = viewModel::onSortChange,
        onNavigateBack = { /*TODO*/ },
        onExportClick = { /*TODO*/ },
        onAboutClick = { /*TODO*/ }
    )

}

@Composable
private fun DreamListScreen(
    listState: DreamFeedState,
    sortConfig: SortConfig,
    showMenu: Boolean,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit,
    onSortChange: (SortConfig) -> Unit,
    onNavigateBack: () -> Unit,
    onExportClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    //TODO: When issue resolved, also show subtitle when collapsed
    //TODO: Possibly adapt nav icon to tablet mode
    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        toolbarTitle = stringResource(
            id = R.string.dream_list_title
        ),
        toolbarSubtitle = (listState as? DreamFeedState.Success)?.let {
            stringResource(
                id = R.string.dream_list_subtitle,
                listState.dreams.size
            )
        },
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_drawer
                ),
                onClick = onNavigateBack
            )
        },
        appbarActions = {
            //TODO: When https://github.com/TrainerSnow/oneui-compose/issues/31 is fixed, place menu somewhere else
            if(showMenu) {
                PopupMenu(
                    onDismissRequest = onMenuClick
                ) {
                    MenuItem(
                        label = stringResource(R.string.dream_list_menu_export),
                        onClick = onExportClick
                    )
                    MenuItem(
                        label = stringResource(R.string.dream_list_menu_about),
                        onClick = onAboutClick
                    )
                }
            }

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
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_more
                ),
                onClick = onMenuClick
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DreamListScreenDefaults.sortSectionPadding),
            horizontalArrangement = Arrangement.End
        ) {
            SortSection(
                sortConfig = sortConfig,
                onSortChange = onSortChange
            )
        }
        DreamList(
            modifier = Modifier
                .fillMaxWidth(),
            listState = listState,
            onDreamClick = onDreamClick,
            onDreamFavouriteClick = onDreamFavouriteClick
        )
    }
}

@Composable
private fun DreamList(
    modifier: Modifier = Modifier,
    listState: DreamFeedState,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit
) {
    DreamFeed(
        modifier = modifier,
        state = listState,
        dreamCallback = object : DreamCallback {

            override fun onClick(dream: Dream) = run { onDreamClick(dream) }

            override fun onFavouriteClick(dream: Dream) = run { onDreamFavouriteClick(dream) }

        }
    )
}

private object DreamListScreenDefaults {

    val sortSectionPadding = PaddingValues(
        horizontal = 12.dp
    )

}

@Preview
@Composable
private fun DreamListScreenPreview() {
    DreamListScreen(
        listState = DreamFeedState
            .Success(
                dreams = DreamPreviewData.dreams,
                temporallySort = true,
                sortConfig = SortConfig(
                    mode = SortMode.Created,
                    direction = SortDirection.Descending
                )
            ),
        sortConfig = SortConfig(
            mode = SortMode.Created,
            direction = SortDirection.Descending
        ),
        showMenu = true,
        onAddClick = { },
        onSearchClick = { },
        onMenuClick = { },
        onDreamClick = { },
        onDreamFavouriteClick = { },
        onSortChange = { },
        onNavigateBack = { },
        onExportClick = {  },
        onAboutClick = { }
    )
}