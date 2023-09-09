package com.snow.diary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.snow.diary.nav.TopLevelDestinations
import com.snow.diary.persons.nav.goToPersonList
import com.snow.feature.dreams.nav.goToDreamList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.oneui.compose.layout.internal.SlidingDrawerState
import org.oneui.compose.layout.internal.rememberSlidingDrawerState

data class DiaryState(

    val navController: NavHostController,

    val drawerState: SlidingDrawerState,

    val scope: CoroutineScope

) {

    var currentNavDest by mutableStateOf(TopLevelDestinations.Dreams)

    fun navigateTo(navDest: TopLevelDestinations) {
        when (navDest) {
            TopLevelDestinations.Dreams -> navController.goToDreamList()
            TopLevelDestinations.Persons -> navController.goToPersonList()
            TopLevelDestinations.Locations -> TODO()
            TopLevelDestinations.Statistics -> TODO()
        }
        currentNavDest = navDest
    }

    fun navigateBack() = navController.popBackStack()

    fun openDrawer() = scope.launch { drawerState.openAnimate() }

}

@Composable
fun rememberDiaryState(
    navController: NavHostController = rememberNavController(),
    drawerState: SlidingDrawerState = rememberSlidingDrawerState(),
    scope: CoroutineScope = rememberCoroutineScope()
) = DiaryState(navController, drawerState, scope)
