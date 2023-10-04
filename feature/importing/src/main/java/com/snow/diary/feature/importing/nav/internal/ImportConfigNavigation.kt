package com.snow.diary.feature.importing.nav.internal

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.importing.screen.config.ImportConfigScreen

internal const val importConfigRoute = "import_config"

internal fun NavController.goToImportConfig(
    navOptions: NavOptions? = null
) = navigate(importConfigRoute, navOptions)

internal fun NavGraphBuilder.importConfig(
    onUriSelected: (Uri) -> Unit
) {
    composable(
        route = importConfigRoute
    ) {
        ImportConfigScreen(
            onUriSelected = onUriSelected
        )
    }
}