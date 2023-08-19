package com.snow.feature.dreams.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.common.time.TimeFormat.formatFullDescription
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
import com.snow.feature.dreams.screen.detail.component.ClearnessProgressBar
import com.snow.feature.dreams.screen.detail.component.HappinessProgressBar
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
    onRelationClick: (Relation) -> Unit
) {
    val dreamState by viewModel.dreamDetailState.collectAsStateWithLifecycle()
    val tabState by viewModel.tabState.collectAsStateWithLifecycle()

    DreamDetailScreen(
        state = dreamState,
        tabState = tabState,
        onNavigateBack = onNavigateBack,
        onTabStateChange = { },
        personCallback = object : PersonCallback {

            override fun onClick(person: Person) { onPersonClick(person) }

            override fun onRelationClick(relation: Relation) { onRelationClick(relation) }

            override fun onFavouriteClick(person: Person) { viewModel.personFavouriteClick(person) }

        },
        onLocationClick = onLocationClick
    )
}

@Composable
private fun DreamDetailScreen(
    state: DreamDetailState,
    tabState: DreamDetailTabState,
    onTabStateChange: (DreamDetailTabState) -> Unit,
    onNavigateBack: () -> Unit,
    personCallback: PersonCallback,
    onLocationClick: (Location) -> Unit
) {
    val title = if (state is DreamDetailState.Success)
        stringResource(
            id = R.string.dream_detail_title,
            state.dream.dream.created.formatFullDescription()
        ) else stringResource(
        id = R.string.dream_detail_title_placeholder
    )

    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        toolbarTitle = title,
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_back),
                onClick = onNavigateBack
            )
        }
    ) {
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Tabs(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        DreamDetailTab.values().forEach { tab ->
                            TabItem(
                                onClick = { onTabStateChange(tabState.copy(tab = tab)) },
                                text = tab.localizedName(),
                                selected = tab == tabState.tab
                            )
                        }
                    }

                    when (tabState.tab) {
                        DreamDetailTab.General -> GeneralTab(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = state,
                            tabState = tabState,
                            onSubtabChange = { subtab ->
                                onTabStateChange(
                                    tabState.copy(
                                        subtab = subtab
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
    }
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
            locations = state.dream.locations
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
            persons = state.dream.persons,
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
        val dream = state.dream.dream
        when (tabState.subtab) {
            DreamDetailSubtab.Content -> {
                TextSeparator(
                    text = stringResource(R.string.dream_detail_content)
                )
                RoundedCornerBox {
                    Text(
                        text = dream.description
                    )
                }

                dream.note?.let { note ->
                    TextSeparator(
                        text = stringResource(R.string.dream_detail_notes)
                    )
                    RoundedCornerBox {
                        Text(
                            text = note
                        )
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

        Tabs(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DreamDetailSubtab.values().forEach { subtab ->
                SubTabItem(
                    onClick = { onSubtabChange(subtab) },
                    text = subtab.localizedName(),
                    selected = tabState.subtab == subtab,
                    enabled = subtab == DreamDetailSubtab.Content ||
                            dream.happiness != null || dream.clearness != null
                )
            }
        }
    }

}

