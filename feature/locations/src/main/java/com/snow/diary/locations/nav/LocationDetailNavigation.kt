package com.snow.diary.locations.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.locations.screen.detail.LocationDetail
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location

private const val locationIdArg = "locationId"
private const val locationDetailName = "location_detail/%s"

private val locationDetailRoute = locationDetailName.format("{$locationIdArg}")

internal class LocationDetailArgs(
    val locationId: Long
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle[locationIdArg]) as Long
    )
}

fun NavController.goToLocationDetail(
    locationId: Long,
    navOptions: NavOptions? = null
) = navigate(
    locationDetailName.format(locationId.toString()),
    navOptions
)

fun NavGraphBuilder.locationDetail(
    onNavigateBack: () -> Unit,
    onEditClick: (Location) -> Unit,
    onDreamClick: (Dream) -> Unit
) {
    composable(
        route = locationDetailRoute,
        arguments = listOf(
            navArgument(
                name = locationIdArg
            ) {
                type = NavType.LongType
            }
        )
    ) {
        LocationDetail(
            onNavigateBack = onNavigateBack,
            onEditClick = onEditClick,
            onDreamClick = onDreamClick
        )
    }
}