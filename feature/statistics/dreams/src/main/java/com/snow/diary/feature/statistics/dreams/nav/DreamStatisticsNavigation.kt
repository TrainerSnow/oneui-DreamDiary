package com.snow.diary.feature.statistics.dreams.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.statistics.dreams.screen.main.DreamStatistics

private const val dreamStatisticsRoute = "dream_statistics"

fun NavController.goToDreamStatistics(navOptions: NavOptions? = null) = navigate(
    dreamStatisticsRoute, navOptions
)

fun NavGraphBuilder.dreamNavigation(
    onNavigateBack: () -> Unit
) {
    composable(
        route = dreamStatisticsRoute
    ) {
        DreamStatistics(
            onNavigateBack = onNavigateBack
        )
    }
}