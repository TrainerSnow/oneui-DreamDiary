package com.snow.diary.feature.dreams.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.core.model.data.Dream
import com.snow.diary.feature.dreams.screen.list.DreamListScreen

const val dream_list_rote = "dream_list"

fun NavController.goToDreamList(navOptions: NavOptions? = null) =
    navigate(dream_list_rote, navOptions)

fun NavGraphBuilder.dreamList(
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onNavigateBack: () -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    composable(
        route = dream_list_rote
    ) {
        DreamListScreen(
            onAddClick = onAddClick,
            onSearchClick = onSearchClick,
            onDreamClick = onDreamClick,
            onNavigateBack = onNavigateBack,
            onExportClick = onExportClick,
            onImportClick = onImportClick,
            onAboutClick = onAboutClick
        )
    }
}