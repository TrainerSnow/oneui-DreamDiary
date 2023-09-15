package com.snow.diary.feature.preferences.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.preferences.screen.main.MainPreferencesScreen

private const val mainPreferencesRoute = "main_preferences"

fun NavController.goToMainPreferences(navOptions: NavOptions? = null) = navigate(mainPreferencesRoute)

fun NavGraphBuilder.mainPreferences(
    onNavigateBack: () -> Unit,
    onNavigateToObfuscationPreferences: () -> Unit
) {
    composable(
        route = mainPreferencesRoute
    ) {
        MainPreferencesScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToObfuscationPreferences = onNavigateToObfuscationPreferences
        )
    }
}