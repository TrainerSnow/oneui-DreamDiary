package com.snow.diary.feature.relations.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.relations.screen.detail.RelationDetail

private const val relationArgId = "relationId"
private const val relationDetailName = "relation_detail/%s"

private val relationDetailRoute = relationDetailName.format("{$relationArgId}")

internal class RelationDetailArgs(
    val relationId: Long
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle[relationArgId]) as Long
    )
}

fun NavController.goToRelationDetail(
    relationId: Long,
    navOptions: NavOptions? = null
) = navigate(
    relationDetailName.format(relationId.toString()),
    navOptions
)

fun NavGraphBuilder.relationDetail(
    onNavigateBack: () -> Unit,
    onEditClick: (Relation) -> Unit,
    onPersonClick: (Person) -> Unit
) {
    composable(
        route = relationDetailRoute,
        arguments = listOf(
            navArgument(
                name = relationArgId
            ) {
                type = NavType.LongType
            }
        )
    ) {
        RelationDetail(
            onNavigateBack = onNavigateBack,
            onEditClick = onEditClick,
            onPersonClick = onPersonClick
        )
    }
}