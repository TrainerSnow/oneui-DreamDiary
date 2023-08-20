package com.snow.diary.ui.feed

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.model.data.Location
import com.snow.diary.ui.R
import com.snow.diary.ui.item.LocationCard
import com.snow.diary.ui.screen.EmptyScreen
import com.snow.diary.ui.screen.ErrorScreen
import com.snow.diary.ui.screen.LoadingScreen
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
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = LocationFeedDefaults.locationItemMinWidth
        )
    ) {
        items(
            count = state.locations.size,
            key = { state.locations[it].id },
            contentType = { LocationFeedDefaults.ctypeLocationItem }
        ) {
            LocationCard(
                location = state.locations[it],
                listPosition = ListPosition.get(state.locations[it], state.locations),
                onLocationClick = onLocationClick
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