package com.snow.diary.ui

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.snow.diary.R
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
import com.snow.diary.feature.preferences.nav.goToMainPreferences
import com.snow.diary.feature.preferences.nav.goToObfuscatePreferences
import com.snow.diary.feature.preferences.nav.mainPreferences
import com.snow.diary.feature.preferences.nav.obfuscatePreferences
import com.snow.diary.feature.relations.nav.addRelation
import com.snow.diary.feature.relations.nav.goToAddRelation
import com.snow.diary.feature.relations.nav.goToRelationDetail
import com.snow.diary.feature.relations.nav.goToRelationList
import com.snow.diary.feature.relations.nav.relationDetail
import com.snow.diary.feature.relations.nav.relationList
import com.snow.diary.feature.statistics.nav.statistics
import com.snow.diary.nav.TopLevelDestinations
import kotlinx.coroutines.launch
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.layout.drawer.DrawerDivider
import org.oneui.compose.layout.drawer.DrawerItem
import org.oneui.compose.layout.drawer.DrawerLayout
import dev.oneuiproject.oneui.R as IconR

@Composable
fun DiaryApplicationRoot(
    state: DiaryState
) {
    val drawerState = state.drawerState
    val context = LocalContext.current

    fun showToast(msg: String) {
        state.scope.launch {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    val obfuscationEnabled by state.obfuscationEnabled.collectAsStateWithLifecycle()
    val locationNum by state.locationsAmountNum.collectAsStateWithLifecycle()
    val dreamsNum by state.dreamsAmountState.collectAsStateWithLifecycle()
    val personsNum by state.personsAmountState.collectAsStateWithLifecycle()

    //TODO: When available, use nav rail not drawer on tablets
    DrawerLayout(
        state = drawerState,
        windowInsets = WindowInsets.systemBars,
        drawerContent = {
            TopLevelDestinations.entries.forEach { navDest ->
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
                    labelEnd = when (navDest) {
                        TopLevelDestinations.Dreams -> dreamsNum?.toString() ?: ""
                        TopLevelDestinations.Persons -> personsNum?.toString() ?: ""
                        TopLevelDestinations.Locations -> locationNum?.toString() ?: ""
                        else -> ""
                    },
                    onClick = { state.navigateTo(navDest) },
                    selected = state.currentNavDest == navDest
                )
            }
        },
        headerIcon = {
            org.oneui.compose.widgets.buttons.IconButton(
                onClick = {
                    state.navController.goToMainPreferences()
                    state.closeDrawer()
                },
                icon = Icon.Resource(IconR.drawable.ic_oui_settings_outline)
            )
        }
    ) {
        DiaryNavHost(state, obfuscationEnabled, ::showToast)
    }
}

@Composable
private fun DiaryNavHost(
    state: DiaryState,
    obfuscationEnabled: Boolean?,
    showToast: (String) -> Unit
) {
    val navController = state.navController

    val obfuscationBlockedMessage = stringResource(R.string.blocked_by_obfuscation)

    fun obfuscationBlocked(block: () -> Unit) {
        if (obfuscationEnabled != null) {
            if (obfuscationEnabled) {
                showToast(obfuscationBlockedMessage)
            } else {
                block()
            }
        }
    }

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = "dream_list"
    ) {
        dreamList(
            onAboutClick = { },
            onAddClick = {
                obfuscationBlocked {
                    navController.goToAddDream()
                }
            },
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
                obfuscationBlocked {
                    navController
                        .goToAddDream(it.id)
                }
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
                obfuscationBlocked {
                    navController.goToAddPerson()
                }
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
                obfuscationBlocked {
                    navController.goToAddPerson(it.id)
                }
            },
            onDreamClick = {
                navController.goToDreamDetail(it.id!!)
            },
            onRelationClick = {
                navController.goToRelationDetail(it.id!!)
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
            onAddLocation = {
                obfuscationBlocked {
                    navController.goToAddLocation()
                }
            },
            onSearchLocation = { },
            onLocationCLick = {
                navController.goToLocationDetail(it.id!!)
            }
        )
        locationDetail(
            onNavigateBack = state::navigateBack,
            onEditClick = {
                obfuscationBlocked {
                    navController.goToAddLocation(it.id)
                }
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
            onAddRelation = {
                obfuscationBlocked {
                    navController.goToAddRelation()
                }
            },
            onSearchRelation = { },
            onRelationClick = {
                navController.goToRelationDetail(it.id!!)
            }
        )
        relationDetail(
            onNavigateBack = state::navigateBack,
            onEditClick = {
                obfuscationBlocked {
                    navController.goToAddRelation(it.id!!)
                }
            },
            onPersonClick = {
                navController.goToPersonDetail(it.id!!)
            }
        )

        mainPreferences(
            onNavigateBack = state::navigateBack,
            onNavigateToObfuscationPreferences = navController::goToObfuscatePreferences
        )
        obfuscatePreferences(
            onNavigateBack = state::navigateBack
        )

        statistics(
            onNavigateBack = state::openDrawer
        )
    }
}