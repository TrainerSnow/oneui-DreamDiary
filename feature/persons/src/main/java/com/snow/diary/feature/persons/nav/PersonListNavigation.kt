package com.snow.diary.feature.persons.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.persons.screen.list.PersonList

private const val personListRoute = "person_list"

fun NavController.goToPersonList(navOptions: NavOptions? = null) = navigate(personListRoute, navOptions)

fun NavGraphBuilder.personList(
    onNavigateBack: () -> Unit,
    onAddPerson: () -> Unit,
    onSearchPerson: () -> Unit,
    onRelationClick: (Relation) -> Unit,
    onPersonClick: (Person) -> Unit,
    onGroupsCLick: () -> Unit
) {
    composable(
        route = personListRoute
    ) {
        PersonList(
            onNavigateBack = onNavigateBack,
            onAddPerson = onAddPerson,
            onSearchPerson = onSearchPerson,
            onRelationClick = onRelationClick,
            onPersonClick = onPersonClick,
            onGroupsClick = onGroupsCLick
        )
    }
}