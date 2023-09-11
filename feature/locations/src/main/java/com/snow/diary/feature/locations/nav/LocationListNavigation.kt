package com.snow.diary.feature.locations.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.locations.screen.list.LocationList
import com.snow.diary.core.model.data.Location

private const val locationListRoute = "location_list"

fun NavController.goToLocationList(navOptions: NavOptions? = null) = navigate(locationListRoute, navOptions)

fun NavGraphBuilder.locationList(
    onNavigateBack: () -> Unit,
    onAddLocation: () -> Unit,
    onSearchLocation: () -> Unit,
    onLocationCLick: (Location) -> Unit
) {
    composable(
        route = locationListRoute
    ) {
        LocationList(
            onNavigateBack = onNavigateBack,
            onAddLocation = onAddLocation,
            onSearchLocation = onSearchLocation,
            onLocationClick = onLocationCLick
        )
    }
}