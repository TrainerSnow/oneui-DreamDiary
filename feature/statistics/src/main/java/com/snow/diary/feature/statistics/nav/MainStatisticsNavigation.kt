package com.snow.diary.feature.statistics.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.statistics.screen.home.MainStatistics

const val statisticsRoute = "statistics"

fun NavController.goToStatistics(navOptions: NavOptions? = null) =
    navigate(statisticsRoute, navOptions)

fun NavGraphBuilder.statistics(
    onNavigateBack: () -> Unit
) {
    composable(
        route = statisticsRoute
    ) {
        MainStatistics(onNavigateBack)
    }
}