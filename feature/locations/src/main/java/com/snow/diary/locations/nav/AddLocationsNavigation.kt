package com.snow.diary.locations.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.locations.screen.add.AddLocation

private const val locationIdArg = "locationId"
private const val addLocationName = "add_location"

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
    navOptions: NavOptions? = null
) = navigate(
    route = locationId?.let { id -> addLocationName.withArg(locationIdArg, locationId.toString()) }
        ?: addLocationName,
    navOptions = navOptions
)

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
}