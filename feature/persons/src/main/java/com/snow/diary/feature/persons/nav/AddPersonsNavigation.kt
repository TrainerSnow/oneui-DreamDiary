package com.snow.diary.feature.persons.nav

import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.snow.diary.feature.persons.screen.add.AddPerson

private const val personIdArg = "personId"
private const val addPersonName = "add_person"
private const val addPersonDialogName = "add_person_dialog"

private fun String.withArg(name: String, value: String): String = "$this?$name=$value"

internal class AddPersonArgs(
    val personId: Long?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        personId = savedStateHandle.get<String>(personIdArg)?.toLongOrNull()
    )
}

fun NavController.goToAddPerson(
    personId: Long? = null,
    dialog: Boolean = false,
    navOptions: NavOptions? = null
) {
    val name = if(dialog) addPersonDialogName else addPersonName
    val route = personId?.let { id -> name.withArg(personIdArg, personId.toString()) }
        ?: name
    navigate(
        route = route,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.addPerson(
    onNavigateBack: () -> Unit
) {
    composable(
        route = addPersonName.withArg(personIdArg, "{$personIdArg}"),
        arguments = listOf(
            navArgument(
                name = personIdArg
            ) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            }
        )
    ) {
        AddPerson(
            onNavigateBack = onNavigateBack
        )
    }
    dialog(
        route = addPersonDialogName.withArg(personIdArg, "{$personIdArg}"),
        arguments = listOf(
            navArgument(
                name = personIdArg
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
        AddPerson(
            onNavigateBack = onNavigateBack
        )
    }
}