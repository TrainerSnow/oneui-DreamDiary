package com.snow.diary.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import com.snow.diary.export.navigation.exportScreen
import com.snow.diary.export.navigation.goToExport
import com.snow.diary.nav.TopLevelDestinations
import com.snow.diary.persons.nav.goToPersonDetail
import com.snow.diary.persons.nav.personDetail
import com.snow.diary.persons.nav.personList
import com.snow.feature.dreams.nav.addDream
import com.snow.feature.dreams.nav.dreamDetail
import com.snow.feature.dreams.nav.dreamList
import com.snow.feature.dreams.nav.goToAddDream
import com.snow.feature.dreams.nav.goToDreamDetail
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.layout.drawer.DrawerDivider
import org.oneui.compose.layout.drawer.DrawerItem
import org.oneui.compose.layout.drawer.DrawerLayout
import dev.oneuiproject.oneui.R as IconR

@Composable
fun DiaryApplicationRoot(
    state: DiaryState = rememberDiaryState()
) {
    val drawerState = state.drawerState

    //TODO: In lib, disable swiping to open drawer
    //TODO: When available, use nav rail not drawer on tablets
    DrawerLayout(
        state = drawerState,
        drawerContent = {
            TopLevelDestinations.values().forEach { navDest ->
                if (navDest == TopLevelDestinations.Statistics) {
                    DrawerDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                DrawerItem(
                    icon = {
                        IconView(
                            icon = navDest.icon
                        )
                    },
                    label = stringResource(navDest.titleRes),
                    onClick = { state.navigateTo(navDest) },
                    selected = state.currentNavDest == navDest
                )
            }
        },
        //TODO: In lib make headerIcon be a composable
        headerIcon = Icon.Resource(IconR.drawable.ic_oui_settings_outline),
        onHeaderIconClick = { TODO("Navigate to settings") }
    ) {
        DiaryNavHost(state)
    }
}

@Composable
private fun DiaryNavHost(
    state: DiaryState
) {
    val navController = state.navController

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = "dream_list",
        enterTransition = {
            fadeIn(tween(0)) //0ms for no transition
        },
        exitTransition = {
            fadeOut(tween(0)) //0ms for no transition
        }
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
            onNavigateBack = state::openDrawer
        )
        dreamDetail(
            onNavigateBack = state::navigateBack,
            onLocationClick = { },
            onPersonClick = {
                navController.goToPersonDetail(it.id!!)
            },
            onRelationClick = { },
            onEditClick = {
                navController
                    .goToAddDream(it.id)
            }
        )
        addDream(
            dismissDream = state::navigateBack
        )
        exportScreen(
            onNavigateBack = state::navigateBack
        )
        personList(
            onNavigateBack = state::openDrawer,
            onAddPerson = { },
            onSearchPerson = { },
            onRelationClick = { },
            onPersonClick = {
                navController.goToPersonDetail(it.id!!)
            }
        )
        personDetail(
            onNavigateBack = state::navigateBack,
            onEditClick = { },
            onDreamClick = {
                navController.goToDreamDetail(it.id!!)
            }
        )
    }
}