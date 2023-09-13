package com.snow.diary.feature.persons.nav

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.persons.screen.detail.PersonDetail

private const val personIdArg = "personId"
private const val personDetailName = "person_detail/%s"

private val personDetailRoute = personDetailName.format("{$personIdArg}")

internal class PersonDetailArgs(
    val personId: Long
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle[personIdArg]) as Long
    )
}

fun NavController.goToPersonDetail(
    personId: Long,
    navOptions: NavOptions? = null
) = navigate(
    personDetailName.format(personId.toString()),
    navOptions
)

fun NavGraphBuilder.personDetail(
    onNavigateBack: () -> Unit,
    onEditClick: (Person) -> Unit,
    onDreamClick: (Dream) -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    composable(
        route = personDetailRoute,
        arguments = listOf(
            navArgument(
                name = personIdArg
            ) {
                type = NavType.LongType
            }
        )
    ) {
        PersonDetail(
            onNavigateBack = onNavigateBack,
            onEditClick = onEditClick,
            onDreamClick = onDreamClick,
            onRelationClick = onRelationClick
        )
    }
}