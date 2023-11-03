package com.snow.diary.feature.preferences.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.preferences.screen.backup.BackupPreferencesScreen

private const val backupPreferencesRoute = "backup_preferences"

fun NavController.goToBackupPreferences(navOptions: NavOptions? = null) = navigate(backupPreferencesRoute)

fun NavGraphBuilder.backupPreferences(
    onNavigateBack: () -> Unit
) {
    composable(
        route = backupPreferencesRoute
    ) {
        BackupPreferencesScreen(
            onNavigateBack = onNavigateBack
        )
    }
}