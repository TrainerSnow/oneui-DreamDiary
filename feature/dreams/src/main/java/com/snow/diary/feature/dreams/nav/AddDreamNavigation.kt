package com.snow.diary.feature.dreams.nav

import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.snow.diary.feature.dreams.screen.add.AddDreamScreen

private const val dreamIdArg = "dreamId"
private const val addDreamName = "add_dream"
private const val addDreamDialogName = "add_dream_dialog"

private fun String.withArg(name: String, value: String): String = "$this?$name=$value"

internal class AddDreamArgs(
    val dreamId: Long?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        dreamId = savedStateHandle.get<String>(dreamIdArg)?.toLongOrNull()
    )
}

fun NavController.goToAddDream(
    dreamId: Long? = null,
    dialog: Boolean = false,
    navOptions: NavOptions? = null
) {
    val name = if(dialog) addDreamDialogName else addDreamName
    val route = dreamId?.let { id -> name.withArg(dreamIdArg, dreamId.toString()) }
        ?: name
    navigate(
        route = route,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.addDream(
    dismissDream: () -> Unit
) {
    composable(
        route = addDreamName.withArg(dreamIdArg, "{$dreamIdArg}"),
        arguments = listOf(
            navArgument(
                name = dreamIdArg
            ) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            }
        )
    ) {
        AddDreamScreen(
            dismissDream = dismissDream
        )
    }
    dialog(
        route = addDreamDialogName.withArg(dreamIdArg, "{$dreamIdArg}"),
        arguments = listOf(
            navArgument(
                name = dreamIdArg
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
        AddDreamScreen(
            dismissDream = dismissDream
        )
    }
}