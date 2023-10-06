package com.snow.diary.feature.persons.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.feed.DreamFeed
import com.snow.diary.core.ui.feed.DreamFeedState
import com.snow.diary.core.ui.screen.ErrorScreen
import com.snow.diary.core.ui.screen.LoadingScreen
import com.snow.diary.feature.persons.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.navigation.TabItem
import org.oneui.compose.navigation.Tabs
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun PersonDetail(
    viewModel: PersonDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditClick: (Person) -> Unit,
    onDreamClick: (Dream) -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()

    PersonDetail(
        state = state,
        tabState = tabs,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onEditClick = onEditClick,
        onDeleteClick = {
            if (state is PersonDetailState.Success) {
                onNavigateBack()
                viewModel.onEvent(PersonDetailEvent.Delete)
            }
        },
        onDreamClick = onDreamClick,
        onRelationClick = onRelationClick
    )
}

@Composable
private fun PersonDetail(
    state: PersonDetailState,
    tabState: PersonDetailTab,
    onEvent: (PersonDetailEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onEditClick: (Person) -> Unit,
    onDeleteClick: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onRelationClick: (Relation) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CollapsingToolbarLayout(
            modifier = Modifier
                .weight(1F),
            toolbarTitle = (state as? PersonDetailState.Success)?.person?.name ?: "",
            appbarNavAction = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_back),
                    onClick = onNavigateBack
                )
            },
            appbarActions = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_edit_outline),
                    enabled = state is PersonDetailState.Success,
                    onClick = {
                        onEditClick((state as PersonDetailState.Success).person)
                    }
                )
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_delete_outline),
                    enabled = state is PersonDetailState.Success,
                    onClick = onDeleteClick
                )
            }
        ) {
            when (state) {
                is PersonDetailState.Error -> ErrorScreen(title = stringResource(R.string.person_detail_note))
                PersonDetailState.Loading -> LoadingScreen(title = stringResource(R.string.person_detail_note))
                is PersonDetailState.Success -> when (tabState) {
                    PersonDetailTab.General -> GeneralSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state,
                        onRelationClick = onRelationClick
                    )

                    PersonDetailTab.Dreams -> DreamsSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state,
                        onDreamClick = onDreamClick,
                        onDreamFavouriteClick = {
                            onEvent(PersonDetailEvent.DreamFavouriteClick(it))
                        }
                    )
                }
            }
        }

        Tabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PersonDetailScreenDefaults.contentPadding)
        ) {
            PersonDetailTab.values().forEach { tab ->
                TabItem(
                    modifier = Modifier
                        .weight(1F),
                    onClick = {
                        onEvent(
                            PersonDetailEvent.ChangeTab(tab)
                        )
                    },
                    text = tab.localizedName(),
                    selected = tab == tabState,
                    enabled = tab == PersonDetailTab.General || !(state as? PersonDetailState.Success)?.dreams.isNullOrEmpty()
                )
            }
        }
    }
}

@Composable
private fun GeneralSection(
    modifier: Modifier = Modifier,
    state: PersonDetailState.Success,
    onRelationClick: (Relation) -> Unit
) {
    val noInfoStyle = TextStyle(
        fontSize = 13.sp,
        color = OneUITheme.colors.seslSecondaryTextColor,
        textAlign = TextAlign.Center
    )
    val textStyle = TextStyle(
        fontSize = 14.sp,
        color = OneUITheme.colors.seslPrimaryTextColor
    )

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        RelationsBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PersonDetailScreenDefaults.contentPadding),
            relations = state.relations,
            onClick = onRelationClick
        )

        if (state.person.notes != null) {
            TextSeparator(
                text = stringResource(R.string.person_detail_note)
            )
            RoundedCornerBox(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = state.person.notes!!,
                    style = textStyle
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1F),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.person_detail_no_info),
                    style = noInfoStyle
                )
            }
        }
    }
}

@Composable
private fun DreamsSection(
    modifier: Modifier = Modifier,
    state: PersonDetailState.Success,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit
) {
    DreamFeed(
        modifier = modifier,
        state = DreamFeedState.Success(state.dreams, true, SortConfig(SortMode.Created)),
        dreamCallback = object : com.snow.diary.core.ui.callback.DreamCallback {

            override fun onClick(dream: Dream) {
                onDreamClick(dream)
            }

            override fun onFavouriteClick(dream: Dream) {
                onDreamFavouriteClick(dream)
            }
        }
    )
}

@Composable
private fun RelationsBar(
    modifier: Modifier = Modifier,
    relations: List<Relation>,
    onClick: (Relation) -> Unit
) = Row(
    modifier = modifier
        .height(PersonDetailScreenDefaults.relationsBarHeight)
        .clip(CircleShape)
) {
    relations.forEach { relation ->
        Box(
            modifier = Modifier
                .background(Color(relation.color))
                .clickable { relation.let(onClick) }
                .weight(1F)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = relation.name,
                textAlign = TextAlign.Center
            )
        }
    }
}

private object PersonDetailScreenDefaults {

    val contentPadding = PaddingValues(
        horizontal = 16.dp
    )

    val relationsBarHeight = 32.dp

}