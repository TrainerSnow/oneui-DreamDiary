package com.snow.feature.dreams.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.common.time.TimeFormat.formatFullDescription
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.ui.callback.PersonCallback
import com.snow.diary.ui.feed.LocationFeed
import com.snow.diary.ui.feed.LocationFeedState
import com.snow.diary.ui.feed.PersonFeed
import com.snow.diary.ui.feed.PersonFeedState
import com.snow.diary.ui.screen.ErrorScreen
import com.snow.diary.ui.screen.LoadingScreen
import com.snow.feature.dreams.R
import com.snow.diary.ui.component.ClearnessProgressBar
import com.snow.feature.dreams.screen.detail.component.DreamDetailEvent
import com.snow.diary.ui.component.HappinessProgressBar
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.navigation.SubTabItem
import org.oneui.compose.navigation.TabItem
import org.oneui.compose.navigation.Tabs
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun DreamDetailScreen(
    viewModel: DreamDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLocationClick: (Location) -> Unit,
    onPersonClick: (Person) -> Unit,
    onRelationClick: (Relation) -> Unit,
    onEditClick: (Dream) -> Unit
) {
    val dreamState by viewModel.dreamDetailState.collectAsStateWithLifecycle()
    val tabState by viewModel.tabState.collectAsStateWithLifecycle()

    DreamDetailScreen(
        state = dreamState,
        tabState = tabState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        personCallback = object : PersonCallback {

            override fun onClick(person: Person) {
                onPersonClick(person)
            }

            override fun onRelationClick(relation: Relation) {
                onRelationClick(relation)
            }

            override fun onFavouriteClick(person: Person) {
                viewModel.onEvent(DreamDetailEvent.PersonFavouriteClick(person))
            }

        },
        onLocationClick = onLocationClick,
        onEditClick = { (dreamState as? DreamDetailState.Success)?.let { onEditClick(it.dream) } },
        onDeleteClick = {
            if (dreamState is DreamDetailState.Success) {
                onNavigateBack()
                viewModel.onEvent(
                    DreamDetailEvent.DeleteDream((dreamState as DreamDetailState.Success).dream)
                )
            }
        }
    )
}

@Composable
private fun DreamDetailScreen(
    state: DreamDetailState,
    tabState: DreamDetailTabState,
    onEvent: (DreamDetailEvent) -> Unit,
    onNavigateBack: () -> Unit,
    personCallback: PersonCallback,
    onLocationClick: (Location) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val title = if (state is DreamDetailState.Success)
        stringResource(
            id = R.string.dream_detail_title,
            state.dream.created.formatFullDescription()
        ) else stringResource(
        id = R.string.dream_detail_title_placeholder
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CollapsingToolbarLayout(
            modifier = Modifier
                .weight(1F),
            toolbarTitle = title,
            appbarNavAction = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_back),
                    onClick = onNavigateBack
                )
            },
            appbarActions = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_edit_outline),
                    onClick = onEditClick,
                    enabled = state is DreamDetailState.Success
                )
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_delete_outline),
                    onClick = onDeleteClick,
                    enabled = state is DreamDetailState.Success
                )
            }
        ) {
            val content = @Composable {
                when (state) {
                    is DreamDetailState.Error -> {
                        ErrorScreen(
                            title = stringResource(
                                id = R.string.dream_detail_title,
                                state.id.toString()
                            ),
                            description = state.msg
                        )
                    }

                    DreamDetailState.Loading -> {
                        LoadingScreen(
                            title = stringResource(
                                id = R.string.dream_detail_loading_title
                            )
                        )
                    }

                    is DreamDetailState.Success -> {
                        when (tabState.tab) {
                            DreamDetailTab.General -> GeneralTab(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = state,
                                tabState = tabState,
                                onSubtabChange = { subtab ->
                                    onEvent(
                                        DreamDetailEvent.ChangeTabState(
                                            tabState.copy(
                                                subtab = subtab
                                            )
                                        )
                                    )
                                }
                            )

                            DreamDetailTab.Persons -> PersonTab(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = state,
                                personCallback = personCallback
                            )

                            DreamDetailTab.Locations -> LocationTab(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = state,
                                onLocationClick = onLocationClick
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(DreamDetailScreenDefaults.contentPadding)
            ) { content() }
        }
        Tabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DreamDetailScreenDefaults.contentPadding)
        ) {
            DreamDetailTab.values().forEach { tab ->
                TabItem(
                    modifier = Modifier
                        .weight(1F),
                    onClick = {
                        onEvent(
                            DreamDetailEvent.ChangeTabState(
                                tabState.copy(
                                    tab = tab
                                )
                            )
                        )
                    },
                    text = tab.localizedName(),
                    selected = tab == tabState.tab,
                    enabled = tab.enabled((state as? DreamDetailState.Success))
                )
            }
        }
    }
}

private fun DreamDetailTab.enabled(state: DreamDetailState.Success?): Boolean = when (this) {
    DreamDetailTab.General -> true
    DreamDetailTab.Persons -> state?.persons?.isNotEmpty() ?: true
    DreamDetailTab.Locations -> state?.locations?.isNotEmpty() ?: true
}

private fun DreamDetailSubtab.enabled(dream: Dream?): Boolean =
    if (dream == null) true else when (this) {
        DreamDetailSubtab.Other -> dream.run { happiness != null || clearness != null } ?: true
        else -> true
    }

@Composable
private fun LocationTab(
    modifier: Modifier = Modifier,
    state: DreamDetailState.Success,
    onLocationClick: (Location) -> Unit
) {
    LocationFeed(
        modifier = modifier,
        state = LocationFeedState.Success(
            locations = state.locations
        ),
        onLocationClick = onLocationClick
    )
}

@Composable
private fun PersonTab(
    modifier: Modifier = Modifier,
    state: DreamDetailState.Success,
    personCallback: PersonCallback
) {
    PersonFeed(
        modifier = modifier,
        state = PersonFeedState.Success(
            persons = state.persons,
            relationSectionSort = false,
            sortConfig = SortConfig()
        ),
        personCallback = personCallback
    )
}

@Composable
private fun GeneralTab(
    modifier: Modifier = Modifier,
    state: DreamDetailState.Success,
    tabState: DreamDetailTabState,
    onSubtabChange: (DreamDetailSubtab) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        val dream = state.dream
        Tabs(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DreamDetailSubtab.values().forEach { subtab ->
                SubTabItem(
                    modifier = Modifier
                        .weight(1F),
                    onClick = { onSubtabChange(subtab) },
                    text = subtab.localizedName(),
                    selected = tabState.subtab == subtab,
                    enabled = subtab.enabled(state.dream)
                )
            }
        }

        when (tabState.subtab) {
            DreamDetailSubtab.Content -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    TextSeparator(
                        text = stringResource(R.string.dream_detail_content)
                    )
                    RoundedCornerBox(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = dream.description
                        )
                    }

                    dream.note?.let { note ->
                        TextSeparator(
                            text = stringResource(R.string.dream_detail_notes)
                        )
                        RoundedCornerBox(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Text(
                                text = note
                            )
                        }
                    }
                }
            }

            DreamDetailSubtab.Other -> {
                dream.happiness?.let { happiness ->
                    TextSeparator(
                        text = stringResource(R.string.dream_detail_happiness)
                    )
                    RoundedCornerBox {
                        HappinessProgressBar(
                            happiness = happiness
                        )
                    }
                }
                dream.clearness?.let { clearness ->
                    TextSeparator(
                        text = stringResource(R.string.dream_detail_clearness)
                    )
                    RoundedCornerBox {
                        ClearnessProgressBar(
                            clearness = clearness
                        )
                    }
                }
            }
        }
    }
}

private object DreamDetailScreenDefaults {

    val contentPadding = PaddingValues(
        horizontal = 16.dp
    )
}

