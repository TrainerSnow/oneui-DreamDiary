package com.snow.diary.feature.preferences.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.preferences.screen.obfuscation.ObfuscationPreferencesScreen

private const val obfuscatePreferencesRoute = "obfuscate_preferences"

fun NavController.goToObfuscatePreferences(navOptions: NavOptions? = null) = navigate(obfuscatePreferencesRoute)

fun NavGraphBuilder.obfuscatePreferences(
    onNavigateBack: () -> Unit
) {
    composable(
        route = obfuscatePreferencesRoute
    ) {
        ObfuscationPreferencesScreen(
            onNavigateBack = onNavigateBack
        )
    }
}