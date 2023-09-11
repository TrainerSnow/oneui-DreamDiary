package com.snow.diary.relations.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import com.snow.diary.relations.R
import com.snow.diary.ui.callback.PersonCallback
import com.snow.diary.ui.feed.PersonFeed
import com.snow.diary.ui.feed.PersonFeedState
import com.snow.diary.ui.screen.ErrorScreen
import com.snow.diary.ui.screen.LoadingScreen
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.navigation.TabItem
import org.oneui.compose.navigation.Tabs
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun RelationDetail(
    viewModel: RelationDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditClick: (Relation) -> Unit,
    onPersonClick: (Person) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()

    RelationDetail(
        state = state,
        tabState = tabs,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onEditClick = onEditClick,
        onPersonClick = onPersonClick
    )
}

@Composable
private fun RelationDetail(
    state: RelationDetailState,
    tabState: RelationDetailTab,
    onEvent: (RelationDetailEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onEditClick: (Relation) -> Unit,
    onPersonClick: (Person) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CollapsingToolbarLayout(
            modifier = Modifier
                .weight(1F),
            toolbarTitle = (state as? RelationDetailState.Success)?.relation?.name ?: "",
            appbarNavAction = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_back),
                    onClick = onNavigateBack
                )
            },
            appbarActions = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_edit_outline),
                    enabled = state is RelationDetailState.Success,
                    onClick = {
                        onEditClick((state as RelationDetailState.Success).relation)
                    }
                )
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_delete_outline),
                    enabled = state is RelationDetailState.Success,
                    onClick = {
                        onNavigateBack()
                        onEvent(
                            RelationDetailEvent.Delete
                        )
                    }
                )
            }
        ) {
            when (state) {
                is RelationDetailState.Error -> ErrorScreen(title = stringResource(R.string.relation_detail_error))
                RelationDetailState.Loading -> LoadingScreen(title = stringResource(R.string.relation_detail_loading))
                is RelationDetailState.Success -> when (tabState) {
                    RelationDetailTab.General -> GeneralSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state
                    )

                    RelationDetailTab.Persons -> PersonsSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state,
                        onPersonClick = onPersonClick,
                        onPersonFavouriteClick = {
                            onEvent(RelationDetailEvent.PersonFavouriteClick(it))
                        }
                    )
                }
            }
        }

        Tabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RelationDetailScreenDefaults.contentPadding)
        ) {
            RelationDetailTab.values().forEach { tab ->
                TabItem(
                    modifier = Modifier
                        .weight(1F),
                    onClick = {
                        onEvent(
                            RelationDetailEvent.ChangeTab(tab)
                        )
                    },
                    text = tab.localizedName(),
                    selected = tab == tabState,
                    enabled = tab == RelationDetailTab.General || !(state as? RelationDetailState.Success)?.persons.isNullOrEmpty()
                )
            }
        }
    }
}

@Composable
private fun GeneralSection(
    modifier: Modifier = Modifier,
    state: RelationDetailState.Success
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RelationDetailScreenDefaults.colorBoxHeight)
                .background(Color(state.relation.color), CircleShape)
                .clip(CircleShape)
        )
        if (state.relation.notes != null) {
            TextSeparator(
                text = stringResource(R.string.relation_detail_note)
            )
            RoundedCornerBox(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = state.relation.notes!!
                )
            }
        }
    }

    //TODO: Show some fancy google maps map here
}

@Composable
private fun PersonsSection(
    modifier: Modifier = Modifier,
    state: RelationDetailState.Success,
    onPersonClick: (Person) -> Unit,
    onPersonFavouriteClick: (Person) -> Unit
) {
    val pwrs = state.persons.map {
        PersonWithRelation(it, state.relation)
    }

    PersonFeed(
        modifier = modifier,
        state = PersonFeedState.Success(pwrs, false, SortConfig(SortMode.Alphabetically)),
        personCallback = object : PersonCallback {
            override fun onClick(person: Person) {
                onPersonClick(person)
            }

            override fun onFavouriteClick(person: Person) {
                onPersonFavouriteClick(person)
            }
        }
    )
}

private object RelationDetailScreenDefaults {

    val contentPadding = PaddingValues(
        horizontal = 16.dp
    )

    val colorBoxHeight = 32.dp

}