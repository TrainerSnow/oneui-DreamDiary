package com.snow.diary.feature.importing.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.importing.screen.main.MainImportScreen

private const val importRoute = "import"

fun NavController.goToImport(navOptions: NavOptions? = null) = navigate(importRoute, navOptions)

fun NavGraphBuilder.import(
    onNavigateBack: () -> Unit
) {
    composable(
        route = importRoute
    ) {
        MainImportScreen (
            onNavigateBack = onNavigateBack
        )
    }
}