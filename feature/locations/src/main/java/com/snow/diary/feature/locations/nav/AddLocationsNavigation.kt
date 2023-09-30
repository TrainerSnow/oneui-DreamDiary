package com.snow.diary.feature.locations.nav

import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.snow.diary.feature.locations.screen.add.AddLocation

private const val locationIdArg = "locationId"
private const val addLocationName = "add_location"
private const val addLocationDialogName = "add_location_dialog"

private fun String.withArg(name: String, value: String): String = "$this?$name=$value"

internal class AddLocationArgs(
    val locationId: Long?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        locationId = savedStateHandle.get<String>(locationIdArg)?.toLongOrNull()
    )
}

fun NavController.goToAddLocation(
    locationId: Long? = null,
    dialog: Boolean = false,
    navOptions: NavOptions? = null
) {
    val name = if(dialog) addLocationDialogName else addLocationName
    val route = locationId?.let { id -> name.withArg(locationIdArg, locationId.toString()) }
        ?: name
    navigate(
        route = route,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.addLocation(
    onNavigateBack: () -> Unit
) {
    composable(
        route = addLocationName.withArg(locationIdArg, "{$locationIdArg}"),
        arguments = listOf(
            navArgument(
                name = locationIdArg
            ) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            }
        )
    ) {
        AddLocation(
            onNavigateBack = onNavigateBack
        )
    }
    dialog(
        route = addLocationDialogName.withArg(locationIdArg, "{$locationIdArg}"),
        arguments = listOf(
            navArgument(
                name = locationIdArg
            ) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            }
        ),
        dialogProperties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        AddLocation(
            onNavigateBack = onNavigateBack
        )
    }

}