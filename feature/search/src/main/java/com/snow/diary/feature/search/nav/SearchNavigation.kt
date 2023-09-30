package com.snow.diary.feature.search.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import com.snow.diary.feature.search.screen.SearchScreen
import com.snow.diary.feature.search.screen.SearchTabs

private const val tabArgId = "tab"
private const val searchRoute = "search/{$tabArgId}"

internal class SearchArgs(
    val tabName: String?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        tabName = savedStateHandle.get<String>(tabArgId)
    )
}

fun NavController.goToSearch(
    tab: SearchTabs = SearchTabs.Dreams,
    navOptions: NavOptions? = null
) {
    val route = searchRoute.replace("{$tabArgId}", tab.name)
    navigate(
        route = route,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.search(
    onNavigateBack: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onPersonClick: (Person) -> Unit,
    onLocationClick: (Location) -> Unit,
) {
    composable(
        route = searchRoute,
        arguments = listOf(
            navArgument(
                name = tabArgId
            ) {
                type = NavType.StringType
            }
        )
    ) {
        SearchScreen(
            onNavigateBack = onNavigateBack,
            onDreamClick = onDreamClick,
            onPersonClick = onPersonClick,
            onLocationClick = onLocationClick
        )
    }
}