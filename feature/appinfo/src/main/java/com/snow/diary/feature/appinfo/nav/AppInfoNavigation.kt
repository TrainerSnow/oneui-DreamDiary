package com.snow.diary.feature.appinfo.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.feature.appinfo.screen.about.AppInfoScreen

private const val AppInfoRoute = "appinfo"

fun NavController.goToAppInfo(navOptions: NavOptions? = null) = navigate(AppInfoRoute, navOptions)

fun NavGraphBuilder.appInfo(
    onNavigateBack: () -> Unit,
    onLicensesClick: () -> Unit
) {
    composable(
        route = AppInfoRoute
    ) {
        AppInfoScreen(
            onNavigateBack = onNavigateBack,
            onLicensesClick = onLicensesClick
        )
    }
}