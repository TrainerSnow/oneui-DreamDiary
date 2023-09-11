package com.snow.diary.core.ui.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.core.model.combine.PersonWithRelation
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.R
import com.snow.diary.core.ui.callback.PersonCallback
import com.snow.diary.core.ui.data.PersonPreviewData
import com.snow.diary.core.ui.item.PersonCard
import com.snow.diary.core.ui.screen.EmptyScreen
import com.snow.diary.core.ui.screen.ErrorScreen
import com.snow.diary.core.ui.screen.LoadingScreen
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.text.TextSeparator

@Composable
fun PersonFeed(
    modifier: Modifier = Modifier,
    state: PersonFeedState,
    personCallback: com.snow.diary.core.ui.callback.PersonCallback = com.snow.diary.core.ui.callback.PersonCallback
) {
    when (state) {
        PersonFeedState.Empty -> {
            EmptyScreen(
                modifier,
                title = stringResource(
                    id = R.string.personfeed_empty_label
                )
            )
        }

        is PersonFeedState.Error -> {
            ErrorScreen(
                modifier = modifier,
                title = stringResource(
                    id = R.string.personfeed_error_label
                ),
                description = state.msg
            )
        }

        PersonFeedState.Loading -> {
            LoadingScreen(
                title = stringResource(
                    id = R.string.personfeed_error_label
                )
            )
        }

        is PersonFeedState.Success -> {
            SuccessFeed(
                state = state,
                personCallback = personCallback
            )
        }
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: PersonFeedState.Success,
    personCallback: com.snow.diary.core.ui.callback.PersonCallback
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = PersonFeedDefaults.personItemMinWidth
        )
    ) {
        val doRelationSort = state.relationSectionSort && state.sortConfig.mode == SortMode.Relation

        if(!doRelationSort) {
            items(
                count = state.persons.size,
                key = { state.persons[it].person.id ?: 0L },
                contentType = { PersonFeedDefaults.ctypePersonitem }
            ) {
                PersonCard(
                    person = state.persons[it].person,
                    relation = state.persons[it].relation,
                    personCallback = personCallback,
                    listPosition = ListPosition.get(state.persons[it], state.persons)
                )
            }
            return@LazyVerticalGrid
        }

        var fromIndex = 0

        while(fromIndex != state.persons.size) {
            val res = relationSortedPersonItems(
                persons = state.persons.subList(fromIndex, state.persons.size),
                personCallback = personCallback,
                relation = state.persons[fromIndex].relation
            )

            fromIndex += res + 1
        }
    }
}


private fun LazyGridScope.relationSortedPersonItems(
    persons: List<PersonWithRelation>,
    personCallback: com.snow.diary.core.ui.callback.PersonCallback = com.snow.diary.core.ui.callback.PersonCallback,
    relation: Relation
): Int {
    val prsns = persons.takeWhile { it.relation == relation }

    if (prsns.isNotEmpty()) {
        separatorItem(relation.name)
        items(
            count = prsns.size,
            key = { prsns[it].person.id ?: 0L },
            contentType = { PersonFeedDefaults.ctypePersonitem }
        ) { index ->
            val person = prsns[index]
            PersonCard(
                person = person.person,
                relation = person.relation,
                personCallback = personCallback,
                listPosition = ListPosition.get(person, prsns)
            )
        }

        return persons.indexOf(prsns.last())
    }

    return -1
}

private fun LazyGridScope.separatorItem(
    title: String
) {
    item(
        key = title,
        span = { GridItemSpan(maxLineSpan) },
        contentType = PersonFeedDefaults.ctypeSeparator
    ) {
        TextSeparator(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private object PersonFeedDefaults {

    val personItemMinWidth = 350.dp

    const val ctypeSeparator = "separator"
    const val ctypePersonitem = "personitem"

}

sealed class PersonFeedState {

    data object Empty : PersonFeedState()

    data class Error(
        val msg: String
    ) : PersonFeedState()

    data object Loading : PersonFeedState()

    /**
     * Persons have been retrieved.
     *
     * [relationSectionSort] should only be true when [persons] are sorted by [Person.relationId]. Otherwise, an [IllegalStateException] is thrown.
     */
    data class Success(
        val persons: List<PersonWithRelation>,
        val relationSectionSort: Boolean,
        val sortConfig: SortConfig
    ) : PersonFeedState()

}

@Preview
@Composable
private fun PersonFeedEmpty(

) = OneUIPreview(title = "PersonFeedEmpty") {
    PersonFeed(
        state = PersonFeedState.Empty
    )
}

@Preview
@Composable
private fun PersonFeedError(

) = OneUIPreview(title = "PersonFeedError") {
    PersonFeed(
        state = PersonFeedState.Error(
            msg = "Error[404]"
        )
    )
}

@Preview
@Composable
private fun PersonFeedLoading(

) = OneUIPreview(title = "PersonFeedLoading") {
    PersonFeed(
        state = PersonFeedState.Loading
    )
}

@Preview
@Composable
private fun PersonFeedSuccess(

) = OneUIPreview(title = "PersonFeedSuccess", padding = PaddingValues()) {
    PersonFeed(
        state = PersonFeedState.Success(
            persons = PersonPreviewData
                .personsWithRelation
                .sortedBy { it.relation.id },
            relationSectionSort = true,
            sortConfig = SortConfig(
                mode = SortMode.Relation
            )
        )
    )
}