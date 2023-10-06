package com.snow.diary.feature.locations.screen.list

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
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.ui.feed.LocationFeed
import com.snow.diary.core.ui.feed.LocationFeedState
import com.snow.diary.core.ui.util.LocationSortModes
import com.snow.diary.core.ui.util.SortSection
import com.snow.diary.core.ui.util.useNavigationDrawer
import com.snow.diary.core.ui.util.windowSizeClass
import com.snow.diary.feature.locations.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun LocationList(
    viewModel: LocationsListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAddLocation: () -> Unit,
    onSearchLocation: () -> Unit,
    onLocationClick: (Location) -> Unit
) {
    val sortConfig by viewModel.sortConfig.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()

    LocationList(
        state = feedState,
        sortConfig = sortConfig,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onAddClick = onAddLocation,
        onSearchClick = onSearchLocation,
        onLocationCLick = onLocationClick
    )
}

@Composable
private fun LocationList(
    state: LocationFeedState,
    sortConfig: SortConfig,
    onEvent: (LocationListEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLocationCLick: (Location) -> Unit
) {
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(R.string.location_list_title),
        toolbarSubtitle = (state as? LocationFeedState.Success)?.let {
            stringResource(
                R.string.location_list_subtitle,
                state.locations.size.toString()
            )
        },
        appbarNavAction = {
            if(windowSizeClass.useNavigationDrawer) {
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
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PersonListScreenDefaults.sortSectionPadding),
            horizontalArrangement = Arrangement.End
        ) {
            SortSection(
                sortConfig = sortConfig,
                onSortChange = {
                    onEvent(LocationListEvent.SortChange(it))
                },
                sortModes = LocationSortModes
            )
        }
        LocationFeed(
            modifier = Modifier
                .fillMaxWidth(),
            state = state,
            onLocationClick = onLocationCLick
        )
    }
}

private object PersonListScreenDefaults {

    val sortSectionPadding = PaddingValues(
        horizontal = 12.dp
    )

}