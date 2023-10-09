package com.snow.diary.feature.dreams.screen.list

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
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.data.DreamPreviewData
import com.snow.diary.core.ui.feed.DreamFeed
import com.snow.diary.core.ui.feed.DreamFeedState
import com.snow.diary.core.ui.util.DreamSortModes
import com.snow.diary.core.ui.util.SortSection
import com.snow.diary.core.ui.util.useNavigationDrawer
import com.snow.diary.core.ui.util.windowSizeClass
import com.snow.diary.feature.dreams.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.menu.MenuItem
import org.oneui.compose.widgets.menu.PopupMenu
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun DreamListScreen(
    viewModel: DreamListViewModel = hiltViewModel(),
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onNavigateBack: () -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onAboutClick: () -> Unit
) {

    val listState by viewModel.dreamListState.collectAsStateWithLifecycle()
    val sortConfig by viewModel.sortConfig.collectAsStateWithLifecycle()
    val showMenu by viewModel.showMenu.collectAsStateWithLifecycle()

    DreamListScreen(
        listState = listState,
        sortConfig = sortConfig,
        onEvent = viewModel::onEvent,
        showMenu = showMenu,
        onAddClick = onAddClick,
        onSearchClick = onSearchClick,
        onDreamClick = onDreamClick,
        onNavigateBack = onNavigateBack,
        onExportClick = onExportClick,
        onImportClick = onImportClick,
        onAboutClick = onAboutClick
    )

}

@Composable
private fun DreamListScreen(
    listState: DreamFeedState,
    sortConfig: SortConfig,
    onEvent: (DreamListEvent) -> Unit,
    showMenu: Boolean,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onNavigateBack: () -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onAboutClick: () -> Unit
) {
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
            if (windowSizeClass.useNavigationDrawer) {
                IconButton(
                    icon = Icon.Resource(
                        IconR.drawable.ic_oui_drawer
                    ),
                    onClick = onNavigateBack
                )
            }
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
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_more
                ),
                onClick = {
                    onEvent(DreamListEvent.MenuClick)
                }
            )
        },
        menu = {
            if (showMenu) {
                PopupMenu(
                    onDismissRequest = {
                        onEvent(DreamListEvent.MenuClick)
                    }
                ) {
                    MenuItem(
                        label = stringResource(R.string.dream_list_menu_export),
                        onClick = onExportClick
                    )
                    MenuItem(
                        label = stringResource(R.string.dream_list_menu_import),
                        onClick = onImportClick
                    )
                }
            }
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
                onSortChange = {
                    onEvent(DreamListEvent.SortChange(it))
                },
                sortModes = DreamSortModes
            )
        }
        DreamList(
            modifier = Modifier
                .fillMaxWidth(),
            listState = listState,
            onDreamClick = onDreamClick,
            onDreamFavouriteClick = {
                onEvent(DreamListEvent.DreamFavouriteClick(it))
            }
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
        dreamCallback = object : com.snow.diary.core.ui.callback.DreamCallback {

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
        onEvent = { },
        showMenu = true,
        onAddClick = { },
        onSearchClick = { },
        onDreamClick = { },
        onNavigateBack = { },
        onExportClick = { },
        onImportClick = { },
        onAboutClick = { }
    )
}