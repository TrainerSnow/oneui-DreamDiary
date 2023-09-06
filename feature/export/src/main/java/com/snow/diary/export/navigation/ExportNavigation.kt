package com.snow.diary.export.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

private const val exportScreenRoute = "export"

fun NavController.goToExport(navOptions: NavOptions? = null) =
    navigate(exportScreenRoute, navOptions)

fun NavGraphBuilder.exportScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = exportScreenRoute
    ) {
        TODO()
    }
}