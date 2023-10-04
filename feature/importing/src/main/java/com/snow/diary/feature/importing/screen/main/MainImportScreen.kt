package com.snow.diary.feature.importing.screen.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.snow.diary.feature.importing.nav.internal.goToImportResult
import com.snow.diary.feature.importing.nav.internal.importConfig
import com.snow.diary.feature.importing.nav.internal.importConfigRoute
import com.snow.diary.feature.importing.nav.internal.importResult

@Composable
internal fun MainImportScreen(
    onNavigateBack: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = importConfigRoute
    ) {
        importResult(
            onNavigateBack = onNavigateBack
        )
        importConfig(
            onUriSelected = { uri ->
                navController.goToImportResult(uri)
            }
        )
    }
}