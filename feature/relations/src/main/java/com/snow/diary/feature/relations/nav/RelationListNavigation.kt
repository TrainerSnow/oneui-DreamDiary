package com.snow.diary.feature.relations.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.relations.screen.list.RelationList

private const val relationListRoute = "relation_list"

fun NavController.goToRelationList(navOptions: NavOptions? = null) = navigate(relationListRoute, navOptions)

fun NavGraphBuilder.relationList(
    onNavigateBack: () -> Unit,
    onAddRelation: () -> Unit,
    onSearchRelation: () -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    composable(
        route = relationListRoute
    ) {
        RelationList(
            onNavigateBack = onNavigateBack,
            onAddRelation = onAddRelation,
            onSearchRelation = onSearchRelation,
            onRelationClick = onRelationClick
        )
    }
}