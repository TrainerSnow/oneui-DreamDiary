package com.snow.feature.dreams.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.feature.dreams.screen.detail.DreamDetailScreen

private const val dreamIdArg = "dreamId"
private const val dreamDetailName = "dream_detail/%s"

private val dreamDetailRoute = dreamDetailName.format("{$dreamIdArg}")

internal class DreamDetailArgs(
    val dreamId: Long
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle[dreamIdArg]) as Long
    )
}

fun NavController.goToDreamDetail(
    dreamId: Long,
    navOptions: NavOptions? = null
) = navigate(
    dreamDetailName.format(dreamId.toString()),
    navOptions
)

fun NavGraphBuilder.dreamDetail(
    onNavigateBack: () -> Unit,
    onLocationClick: (Location) -> Unit,
    onPersonClick: (Person) -> Unit,
    onRelationClick: (Relation) -> Unit,
    onEditClick: (Dream) -> Unit
) {
    composable(
        route = dreamDetailRoute,
        arguments = listOf(
            navArgument(
                name = dreamIdArg
            ) {
                type = NavType.LongType
            }
        )
    ) {
        DreamDetailScreen(
            onNavigateBack = onNavigateBack,
            onLocationClick = onLocationClick,
            onPersonClick = onPersonClick,
            onRelationClick = onRelationClick,
            onEditClick = onEditClick
        )
    }
}