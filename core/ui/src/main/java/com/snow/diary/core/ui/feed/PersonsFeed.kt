package com.snow.diary.core.ui.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.ui.R
import com.snow.diary.core.ui.callback.PersonCallback
import com.snow.diary.core.ui.data.PersonPreviewData
import com.snow.diary.core.ui.item.PersonCard
import com.snow.diary.core.ui.screen.EmptyScreen
import com.snow.diary.core.ui.screen.ErrorScreen
import com.snow.diary.core.ui.screen.LoadingScreen
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview

@Composable
fun PersonFeed(
    modifier: Modifier = Modifier,
    state: PersonFeedState,
    personCallback: PersonCallback = PersonCallback
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
    personCallback: PersonCallback
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = PersonFeedDefaults.personItemMinWidth
        )
    ) {
        items(
            count = state.persons.size,
            key = { state.persons[it].person.id ?: 0L },
            contentType = { PersonFeedDefaults.ctypePersonitem }
        ) {
            PersonCard(
                person = state.persons[it].person,
                relations = state.persons[it].relation,
                personCallback = personCallback,
                listPosition = ListPosition.get(state.persons[it], state.persons)
            )
        }
        return@LazyVerticalGrid
    }
}

private object PersonFeedDefaults {

    val personItemMinWidth = 350.dp

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
     */
    data class Success(
        val persons: List<PersonWithRelations>
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
                .personsWithRelations
        )
    )
}