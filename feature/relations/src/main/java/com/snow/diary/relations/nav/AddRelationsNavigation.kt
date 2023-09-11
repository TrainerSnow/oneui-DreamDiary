package com.snow.diary.relations.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.relations.screen.add.AddRelation

private const val relationArgId = "relationId"
private const val addRelationName = "add_relation"

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
    navOptions: NavOptions? = null
) = navigate(
    route = relationId?.let { id -> addRelationName.withArg(relationArgId, relationId.toString()) }
        ?: addRelationName,
    navOptions = navOptions
)

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
}