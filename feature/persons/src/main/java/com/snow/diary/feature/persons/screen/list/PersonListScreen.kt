package com.snow.diary.feature.persons.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.feature.persons.R
import com.snow.diary.core.ui.callback.PersonCallback
import com.snow.diary.core.ui.feed.PersonFeed
import com.snow.diary.core.ui.feed.PersonFeedState
import com.snow.diary.core.ui.util.SortSection
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun PersonList(
    viewModel: PersonListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAddPerson: () -> Unit,
    onSearchPerson: () -> Unit,
    onRelationClick: (Relation) -> Unit,
    onPersonClick: (Person) -> Unit,
    onGroupsClick: () -> Unit
) {
    val sortConfig by viewModel.sortConfig.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()

    PersonList(
        state = feedState,
        sortConfig = sortConfig,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onAddClick = onAddPerson,
        onSearchClick = onSearchPerson,
        onRelationCLick = onRelationClick,
        onPersonClick = onPersonClick,
        onGroupsClick = onGroupsClick
    )
}

@Composable
private fun PersonList(
    state: PersonFeedState,
    sortConfig: SortConfig,
    onEvent: (PersonListEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    onRelationCLick: (Relation) -> Unit,
    onPersonClick: (Person) -> Unit,
    onGroupsClick: () -> Unit
) {
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(R.string.person_list_title),
        toolbarSubtitle = (state as? PersonFeedState.Success)?.let {
            stringResource(
                R.string.person_list_subtitle,
                state.persons.size.toString()
            )
        },
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_drawer),
                onClick = onNavigateBack
            )
        },
        appbarActions = {
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_add
                ),
                onClick = onAddClick
            )
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_search
                ),
                onClick = onSearchClick
            )
            IconButton(
                icon = Icon.Resource(
                    IconR.drawable.ic_oui_community
                ),
                onClick = onGroupsClick
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PersonListScreenDefaults.sortSectionPadding),
            horizontalArrangement = Arrangement.End
        ) {
            SortSection(
                sortConfig = sortConfig,
                onSortChange = {
                    onEvent(PersonListEvent.SortChange(it))
                }
            )
        }
        PersonFeed(
            modifier = Modifier
                .fillMaxWidth(),
            state = state,
            personCallback = object : com.snow.diary.core.ui.callback.PersonCallback {
                override fun onClick(person: Person) {
                    onPersonClick(person)
                }

                override fun onRelationClick(relation: Relation) {
                    onRelationCLick(relation)
                }

                override fun onFavouriteClick(person: Person) {
                    onEvent(PersonListEvent.PersonFavouriteClick(person))
                }
            }
        )
    }
}

private object PersonListScreenDefaults {

    val sortSectionPadding = PaddingValues(
        horizontal = 12.dp
    )

}