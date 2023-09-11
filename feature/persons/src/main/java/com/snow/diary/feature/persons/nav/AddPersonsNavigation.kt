package com.snow.diary.feature.persons.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.feature.persons.screen.add.AddPerson

private const val personIdArg = "personId"
private const val addPersonName = "add_person"

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
    navOptions: NavOptions? = null
) = navigate(
    route = personId?.let { id -> addPersonName.withArg(personIdArg, personId.toString()) }
        ?: addPersonName,
    navOptions = navOptions
)

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
}