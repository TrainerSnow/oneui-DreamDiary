package com.snow.diary.core.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.ui.R
import com.snow.diary.core.ui.item.LocationCard
import com.snow.diary.core.ui.screen.EmptyScreen
import com.snow.diary.core.ui.screen.ErrorScreen
import com.snow.diary.core.ui.screen.LoadingScreen
import com.snow.diary.core.ui.util.windowSizeClass
import org.oneui.compose.util.ListPosition


@Composable
fun LocationFeed(
    modifier: Modifier = Modifier,
    state: LocationFeedState,
    onLocationClick: (Location) -> Unit
) {
    when (state) {
        LocationFeedState.Empty -> {
            EmptyScreen(
                modifier,
                title = stringResource(
                    id = R.string.locationfeed_empty_label
                )
            )
        }

        is LocationFeedState.Error -> {
            ErrorScreen(
                modifier = modifier,
                title = stringResource(
                    id = R.string.locationfeed_error_label
                ),
                description = state.msg
            )
        }

        LocationFeedState.Loading -> {
            LoadingScreen(
                title = stringResource(
                    id = R.string.locationfeed_error_label
                )
            )
        }

        is LocationFeedState.Success -> {
            SuccessFeed(
                state = state,
                onLocationClick = onLocationClick
            )
        }
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: LocationFeedState.Success,
    onLocationClick: (Location) -> Unit
) {
    val doListPositions = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = LocationFeedDefaults.locationItemMinWidth
        ),
        horizontalArrangement = if(doListPositions) Arrangement.Start
        else Arrangement.spacedBy(4.dp),
        verticalArrangement = if(doListPositions) Arrangement.Top else Arrangement.spacedBy(4.dp)
    ) {
        items(
            count = state.locations.size,
            key = { state.locations[it].id ?: 0L },
            contentType = { LocationFeedDefaults.ctypeLocationItem }
        ) {
            val pos = if (doListPositions) ListPosition.get(state.locations[it], state.locations)
            else ListPosition.Single

            LocationCard(
                location = state.locations[it],
                listPosition = pos,
                onClick = onLocationClick
            )
        }
    }
}

private object LocationFeedDefaults {

    val locationItemMinWidth = 350.dp

    const val ctypeLocationItem = "locationItem"

}

sealed class LocationFeedState {

    data object Empty : LocationFeedState()

    data class Error(
        val msg: String
    ) : LocationFeedState()

    data object Loading : LocationFeedState()

    /**
     * Persons have been retrieved.
     */
    data class Success(
        val locations: List<Location>
    ) : LocationFeedState()

}