package com.snow.diary.feature.statistics.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.statistics.screen.dream.DreamStatistics

private const val dreamStatisticsRoute = "dream_statistics"

internal fun NavController.goToDreamStatistics(navOptions: NavOptions? = null) = navigate(
    dreamStatisticsRoute, navOptions
)

internal fun NavGraphBuilder.dreamNavigation(
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