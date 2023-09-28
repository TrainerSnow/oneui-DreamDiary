package com.snow.diary.feature.statistics.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.statistics.screen.locations.LocationsStatistics

private const val locationsStatisticsRoute = "locations_statistic"

internal fun NavController.goToLocationsStatistics(navOptions: NavOptions? = null) =
    navigate(locationsStatisticsRoute, navOptions)

internal fun NavGraphBuilder.locationsStatistics(
    onNavigateBack: () -> Unit
) {
    composable(
        route = locationsStatisticsRoute
    ) {
        LocationsStatistics(
            onNavigateBack = onNavigateBack
        )
    }
}