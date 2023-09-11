package com.snow.diary.feature.export.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.export.screen.ExportScreen

private const val exportScreenRoute = "export"

fun NavController.goToExport(navOptions: NavOptions? = null) =
    navigate(exportScreenRoute, navOptions)

fun NavGraphBuilder.exportScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = exportScreenRoute
    ) {
        ExportScreen(
            onNavigateBack = onNavigateBack
        )
    }
}