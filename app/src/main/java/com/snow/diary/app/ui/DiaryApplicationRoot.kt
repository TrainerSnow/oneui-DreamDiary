package com.snow.diary.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import com.snow.diary.app.nav.TopLevelDestinations
import com.snow.diary.feature.dreams.nav.addDream
import com.snow.diary.feature.dreams.nav.dreamDetail
import com.snow.diary.feature.dreams.nav.dreamList
import com.snow.diary.feature.dreams.nav.goToAddDream
import com.snow.diary.feature.dreams.nav.goToDreamDetail
import com.snow.diary.feature.export.navigation.exportScreen
import com.snow.diary.feature.export.navigation.goToExport
import com.snow.diary.feature.locations.nav.addLocation
import com.snow.diary.feature.locations.nav.goToAddLocation
import com.snow.diary.feature.locations.nav.goToLocationDetail
import com.snow.diary.feature.locations.nav.locationDetail
import com.snow.diary.feature.locations.nav.locationList
import com.snow.diary.feature.persons.nav.addPerson
import com.snow.diary.feature.persons.nav.goToAddPerson
import com.snow.diary.feature.persons.nav.goToPersonDetail
import com.snow.diary.feature.persons.nav.personDetail
import com.snow.diary.feature.persons.nav.personList
import com.snow.diary.feature.relations.nav.addRelation
import com.snow.diary.feature.relations.nav.goToAddRelation
import com.snow.diary.feature.relations.nav.goToRelationDetail
import com.snow.diary.feature.relations.nav.goToRelationList
import com.snow.diary.feature.relations.nav.relationDetail
import com.snow.diary.feature.relations.nav.relationList
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
        startDestination = "dream_list"/*,
        enterTransition = {
            fadeIn(tween(0)) //0ms for no transition
        },
        exitTransition = {
            fadeOut(tween(0)) //0ms for no transition
        }*/
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
            onLocationClick = {
                navController.goToLocationDetail(it.id!!)
            },
            onPersonClick = {
                navController.goToPersonDetail(it.id!!)
            },
            onRelationClick = {
                navController.goToRelationDetail(it.id!!)
            },
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
            onAddPerson = {
                navController.goToAddPerson()
            },
            onSearchPerson = { },
            onRelationClick = {
                navController.goToRelationDetail(it.id!!)
            },
            onPersonClick = {
                navController.goToPersonDetail(it.id!!)
            },
            onGroupsCLick = navController::goToRelationList
        )
        personDetail(
            onNavigateBack = state::navigateBack,
            onEditClick = {
                navController.goToAddPerson(it.id)
            },
            onDreamClick = {
                navController.goToDreamDetail(it.id!!)
            }
        )
        addPerson(
            onNavigateBack = state::navigateBack
        )

        addLocation(
            onNavigateBack = state::navigateBack
        )
        locationList(
            onNavigateBack = state::openDrawer,
            onAddLocation = navController::goToAddLocation,
            onSearchLocation = { },
            onLocationCLick = {
                navController.goToLocationDetail(it.id!!)
            }
        )
        locationDetail(
            onNavigateBack = state::navigateBack,
            onEditClick = {
                navController.goToAddLocation(it.id)
            },
            onDreamClick = {
                navController.goToDreamDetail(it.id!!)
            }
        )

        addRelation(
            onNavigateBack = state::navigateBack
        )
        relationList(
            onNavigateBack = state::navigateBack,
            onAddRelation = navController::goToAddRelation,
            onSearchRelation = { },
            onRelationClick = {
                navController.goToRelationDetail(it.id!!)
            }
        )
        relationDetail(
            onNavigateBack = state::navigateBack,
            onEditClick = {
                navController.goToAddRelation(it.id!!)
            },
            onPersonClick = {
                navController.goToPersonDetail(it.id!!)
            }
        )
    }
}