package com.snow.diary.feature.relations.nav

import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.snow.diary.feature.relations.screen.add.AddRelation

private const val relationArgId = "relationId"
private const val addRelationName = "add_relation"
private const val addRelationDialogName = "add_relation_dialog"

private fun String.withArg(name: String, value: String): String = "$this?$name=$value"

internal class AddRelationArgs(
    val relationId: Long?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        relationId = savedStateHandle.get<String>(relationArgId)?.toLongOrNull()
    )
}

fun NavController.goToAddRelation(
    relationId: Long? = null,
    dialog: Boolean = false,
    navOptions: NavOptions? = null
) {
    val name = if(dialog) addRelationDialogName else addRelationName
    val route = relationId?.let { id -> name.withArg(relationArgId, relationId.toString()) }
        ?: name
    navigate(
        route = route,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.addRelation(
    onNavigateBack: () -> Unit
) {
    composable(
        route = addRelationName.withArg(relationArgId, "{$relationArgId}"),
        arguments = listOf(
            navArgument(
                name = relationArgId
            ) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            }
        )
    ) {
        AddRelation(
            onNavigateBack = onNavigateBack
        )
    }
    dialog(
        route = addRelationDialogName.withArg(relationArgId, "{$relationArgId}"),
        arguments = listOf(
            navArgument(
                name = relationArgId
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
        AddRelation(
            onNavigateBack = onNavigateBack
        )
    }
}