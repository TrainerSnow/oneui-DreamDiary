package com.snow.diary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.snow.diary.export.navigation.exportScreen
import com.snow.diary.export.navigation.goToExport
import com.snow.feature.dreams.nav.addDream
import com.snow.feature.dreams.nav.dreamDetail
import com.snow.feature.dreams.nav.dreamList
import com.snow.feature.dreams.nav.goToAddDream
import com.snow.feature.dreams.nav.goToDreamDetail
import org.oneui.compose.theme.OneUITheme

@Composable
fun DiaryApplicationRoot(
    state: DiaryState = rememberDiaryState()
) {
    val navController = state.navController

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(OneUITheme.colors.seslRoundAndBgcolor),
        navController = navController,
        startDestination = "dream_list"
    ) {
        dreamList(
            onAboutClick = { },
            onAddClick = navController::goToAddDream,
            onSearchClick = { },
            onDreamClick = { dream ->
                navController
                    .goToDreamDetail(dream.id!!)
            },
            onExportClick = navController::goToExport,
            onNavigateBack = { }
        )
        dreamDetail(
            onNavigateBack = navController::navigateUp,
            onLocationClick = { },
            onPersonClick = { },
            onRelationClick = { },
            onEditClick = {
                navController
                    .goToAddDream(it.id)
            }
        )
        addDream(
            dismissDream = navController::navigateUp
        )
        exportScreen(
            onNavigateBack = navController::navigateUp
        )
    }
}