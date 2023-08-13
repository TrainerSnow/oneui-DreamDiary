package com.snow.diary.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.R
import com.snow.diary.ui.data.PersonPreviewData
import com.snow.diary.ui.item.PersonCard
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.base.iconColors
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.text.TextSeparator

@Composable
fun PersonFeed(
    modifier: Modifier = Modifier,
    state: PersonFeedState,
    onPersonClick: (Person) -> Unit,
    onPersonFavouriteClick: (Person) -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    when (state) {
        PersonFeedState.Empty -> {
            EmptyFeed(modifier)
        }

        is PersonFeedState.Error -> {
            ErrorFeed(modifier, state)
        }

        PersonFeedState.Loading -> {
            LoadingFeed(modifier)
        }

        is PersonFeedState.Success -> {
            SuccessFeed(
                state = state,
                onPersonClick = onPersonClick,
                onPersonFavouriteClick = onPersonFavouriteClick,
                onRelationClick = onRelationClick
            )
        }
    }
}

@Composable
private fun SuccessFeed(
    modifier: Modifier = Modifier,
    state: PersonFeedState.Success,
    onPersonClick: (Person) -> Unit,
    onPersonFavouriteClick: (Person) -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(
            minSize = PersonFeedDefaults.personItemMinWidth
        )
    ) {
        if(!state.relationSectionSort) {
            items(
                count = state.persons.size,
                key = { state.persons[it].person.id },
                contentType = { PersonFeedDefaults.ctypePersonitem }
            ) {
                PersonCard(
                    person = state.persons[it].person,
                    relation = state.persons[it].relation,
                    onClick = onPersonClick,
                    onRelationClick = { onRelationClick(state.persons[it].relation) },
                    onFavouriteClick = { onPersonFavouriteClick(state.persons[it].person) },
                    listPosition = ListPosition.get(state.persons[it], state.persons)
                )
            }
            return@LazyVerticalGrid
        }

        require(state.sortConfig.mode == SortMode.Relation) { "relationSectionSort is enabled, but the SortConfig is not sorting by relation." }

        var fromIndex = 0

        while(fromIndex != state.persons.size) {
            val res = relationSortedPersonItems(
                persons = state.persons.subList(fromIndex, state.persons.size),
                onPersonClick = onPersonClick,
                onPersonFavouriteClick = onPersonFavouriteClick,
                onRelationClick = onRelationClick,
                relation = state.persons[fromIndex].relation
            )

            fromIndex += res + 1
        }
    }
}


private fun LazyGridScope.relationSortedPersonItems(
    persons: List<PersonWithRelation>,
    onPersonClick: (Person) -> Unit,
    onPersonFavouriteClick: (Person) -> Unit,
    onRelationClick: (Relation) -> Unit,
    relation: Relation
): Int {
    val prsns = persons.takeWhile { it.relation == relation }

    if (prsns.isNotEmpty()) {
        separatorItem(relation.name)
        items(
            count = prsns.size,
            key = { prsns[it].person.id },
            contentType = { PersonFeedDefaults.ctypePersonitem }
        ) { index ->
            val person = prsns[index]
            PersonCard(
                person = person.person,
                relation = person.relation,
                onClick = { onPersonClick(person.person) },
                onRelationClick = { onRelationClick(person.relation) },
                onFavouriteClick = { onPersonFavouriteClick(person.person) },
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

@Composable
private fun EmptyFeed(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(PersonFeedDefaults.emptyIconTextSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val style = TextStyle(
            color = OneUITheme.colors.seslSecondaryTextColor,
            fontSize = 12.sp
        )

        IconView(
            icon = Icon.Resource(dev.oneuiproject.oneui.R.drawable.ic_oui_lasso_add),
            colors = iconColors(
                tint = OneUITheme.colors.seslSecondaryTextColor.copy(
                    alpha = 0.4F
                )
            ),
            modifier = Modifier
                .size(PersonFeedDefaults.emptyIconSize),
            contentDescription = stringResource(
                id = R.string.personfeed_empty_label
            )
        )
        Text(
            text = stringResource(
                id = R.string.personfeed_empty_label
            ),
            style = style
        )
    }
}

@Composable
private fun ErrorFeed(
    modifier: Modifier = Modifier,
    state: PersonFeedState.Error
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val warningStyle = TextStyle(
            color = OneUITheme.colors.seslErrorColor,
            fontSize = 12.sp
        )

        IconView(
            icon = Icon.Resource(dev.oneuiproject.oneui.R.drawable.ic_oui_error),
            colors = iconColors(
                tint = OneUITheme.colors.seslErrorColor
            ),
            modifier = Modifier
                .size(PersonFeedDefaults.errorIconSize),
            contentDescription = stringResource(
                id = R.string.personfeed_error_label
            )
        )
        Spacer(
            modifier = Modifier
                .height(PersonFeedDefaults.errorIconTextSpacing)
        )
        Text(
            text = stringResource(
                id = R.string.personfeed_error_label
            ),
            style = warningStyle
        )
        Spacer(
            modifier = Modifier
                .height(PersonFeedDefaults.errorTextMsgSPacing)
        )
        Text(
            text = state.msg,
            style = warningStyle
        )
    }
}

@Composable
private fun LoadingFeed(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(PersonFeedDefaults.loadingIconTextSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val style = TextStyle(
            color = OneUITheme.colors.seslPrimaryTextColor,
            fontSize = 12.sp
        )

        ProgressIndicator(
            type = ProgressIndicatorType.CircularIndeterminate(
                size = CircularProgressIndicatorSize.Companion.Large
            )
        )
        Text(
            text = stringResource(
                id = R.string.personfeed_loading_label
            ),
            style = style
        )
    }
}

private object PersonFeedDefaults {

    val emptyIconSize = 65.dp
    val emptyIconTextSpacing = 16.dp

    val loadingIconTextSpacing = 16.dp

    val errorIconSize = 65.dp
    val errorIconTextSpacing = 16.dp
    val errorTextMsgSPacing = 4.dp

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
        state = PersonFeedState.Empty,
        onPersonClick = { },
        onPersonFavouriteClick = { },
        onRelationClick = { }
    )
}

@Preview
@Composable
private fun PersonFeedError(

) = OneUIPreview(title = "PersonFeedError") {
    PersonFeed(
        state = PersonFeedState.Error(
            msg = "Error[404]"
        ),
        onPersonClick = { },
        onPersonFavouriteClick = { },
        onRelationClick = { }
    )
}

@Preview
@Composable
private fun PersonFeedLoading(

) = OneUIPreview(title = "PersonFeedLoading") {
    PersonFeed(
        state = PersonFeedState.Loading,
        onPersonClick = { },
        onPersonFavouriteClick = { },
        onRelationClick = { }
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
        ),
        onPersonClick = { },
        onPersonFavouriteClick = { },
        onRelationClick = { }
    )
}