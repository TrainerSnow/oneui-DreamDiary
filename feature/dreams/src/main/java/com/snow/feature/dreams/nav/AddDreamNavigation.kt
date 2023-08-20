package com.snow.feature.dreams.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val dreamIdArg = "dreamId"
private const val addDreamName = "add_dream"

private fun String.withArg(name: String, value: String): String = "$this?$name=$value"

internal class AddDreamArgs(
    val dreamId: Long
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle[dreamIdArg]) as Long
    )
}

fun NavController.goToAddDream(
    dreamId: Long? = null,
    navOptions: NavOptions? = null
) = navigate(
    route = dreamId?.let { id -> addDreamName.withArg(dreamIdArg, dreamId.toString()) } ?: addDreamName,
    navOptions = navOptions
)

fun NavGraphBuilder.addDream(

) {
    composable(
        route = addDreamName.withArg(dreamIdArg, dreamIdArg),
        arguments = listOf(
            navArgument(
                name = dreamIdArg
            ) {
                nullable = true
                defaultValue = null
                type = NavType.LongType
            }
        )
    ) {
        //TODO: Call composable here
    }
}