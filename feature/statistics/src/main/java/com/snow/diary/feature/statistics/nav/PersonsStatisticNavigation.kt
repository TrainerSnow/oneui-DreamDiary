package com.snow.diary.feature.statistics.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.statistics.screen.persons.PersonsStatistics

private const val personsStatisticsRoute = "persons_statistic"

internal fun NavController.goToPersonsStatistics(navOptions: NavOptions? = null) =
    navigate(personsStatisticsRoute, navOptions)

internal fun NavGraphBuilder.personsStatistics(
    onNavigateBack: () -> Unit
) {
    composable(
        route = personsStatisticsRoute
    ) {
        PersonsStatistics(
            onNavigateBack = onNavigateBack
        )
    }
}