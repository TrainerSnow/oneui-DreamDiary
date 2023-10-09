package com.snow.diary.feature.appinfo.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.appinfo.screen.licenses.LicensesScreen

private const val LicensesRoute = "licenses"

fun NavController.goToLicenses(navOptions: NavOptions? = null) = navigate(LicensesRoute, navOptions)

fun NavGraphBuilder.licenses(
    onNavigateBack: () -> Unit
) {
    composable(
        route = LicensesRoute
    ) {
        LicensesScreen(
            onNavigateBack = onNavigateBack
        )
    }
}