package com.snow.diary.feature.search.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.callback.DreamCallback
import com.snow.diary.core.ui.callback.PersonCallback
import com.snow.diary.core.ui.component.DateRangeDialog
import com.snow.diary.core.ui.feed.DreamFeed
import com.snow.diary.core.ui.feed.DreamFeedState
import com.snow.diary.core.ui.feed.LocationFeed
import com.snow.diary.core.ui.feed.LocationFeedState
import com.snow.diary.core.ui.feed.PersonFeed
import com.snow.diary.core.ui.feed.PersonFeedState
import com.snow.diary.feature.search.R
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.navigation.TabItem
import org.oneui.compose.navigation.Tabs
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.SearchView
import org.oneui.compose.widgets.SearchViewButton
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),

    onNavigateBack: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onPersonClick: (Person) -> Unit,
    onLocationClick: (Location) -> Unit,
) {
    val state by viewModel.searchState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onDreamClick = onDreamClick,
        onPersonClick = onPersonClick,
        onLocationClick = onLocationClick
    )
}

@Composable
private fun SearchScreen(
    state: SearchState,
    uiState: SearchUiState,
    onEvent: (SearchEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onDreamClick: (Dream) -> Unit,
    onPersonClick: (Person) -> Unit,
    onLocationClick: (Location) -> Unit,
) {
    if (uiState.showRangeDialog) {
        DateRangeDialog(
            onDismissRequest = { onEvent(SearchEvent.ToggleRangeDialog) },
            selected = uiState.range,
            onRangeSelect = { onEvent(SearchEvent.ChangeRange(it)) }
        )
    }

    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OneUITheme.colors.seslRoundAndBgcolor)
            .imePadding(),
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        SearchView(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            query = uiState.query,
            onQueryChange = { onEvent(SearchEvent.ChangeQuery(it)) },
            hint = stringResource(uiState.selectedTab.queryHint),
            enabled = true,
            backButton = {
                SearchViewButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_back),
                    onClick = onNavigateBack
                )
            },
            closeButton = {
                SearchViewButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_close),
                    onClick = {
                        onEvent(
                            SearchEvent.ChangeQuery("")
                        )
                    }
                )
            },
            extraButton = {
                SearchViewButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_calendar_month),
                    onClick = {
                        onEvent(
                            SearchEvent.ToggleRangeDialog
                        )
                    }
                )
            }
        )
        LaunchedEffect(
            key1 = uiState.selectedTab,
        ){
            focusRequester.requestFocus()
        }

        Tabs(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SearchTabs.entries.forEach { tab ->
                TabItem(
                    modifier = Modifier
                        .weight(1F),
                    onClick = { onEvent(SearchEvent.ChangeTab(tab)) },
                    text = stringResource(tab.title),
                    selected = tab == uiState.selectedTab
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            when (state) {
                SearchState.NoQuery -> {
                    ErrorScreen(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = stringResource(
                            R.string.search_no_query,
                            stringResource(uiState.selectedTab.title)
                        ),
                        icon = Icon.Resource(IconR.drawable.ic_oui_image_search)
                    )
                }

                SearchState.NoResults -> {
                    ErrorScreen(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = stringResource(R.string.search_no_results),
                        icon = Icon.Resource(IconR.drawable.ic_oui_error_2)
                    )
                }

                SearchState.Searching -> {
                    val textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = OneUITheme.colors.seslSecondaryTextColor,
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement
                            .spacedBy(12.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProgressIndicator(
                            type = ProgressIndicatorType.CircularIndeterminate(
                                size = CircularProgressIndicatorSize.Companion.Medium
                            )
                        )
                        Text(
                            text = stringResource(R.string.search_locations),
                            style = textStyle
                        )
                    }
                }

                is SearchState.Success -> {
                    Feed(
                        modifier = Modifier.fillMaxSize(),
                        state,
                        tab = uiState.selectedTab,
                        onEvent,
                        onDreamClick,
                        onPersonClick,
                        onLocationClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Feed(
    modifier: Modifier = Modifier,
    state: SearchState.Success,
    tab: SearchTabs,
    onEvent: (SearchEvent) -> Unit,
    onDreamClick: (Dream) -> Unit,
    onPersonClick: (Person) -> Unit,
    onLocationClick: (Location) -> Unit,
) {
    when (tab) {
        SearchTabs.Dreams -> {
            DreamFeed(
                modifier = modifier,
                state = DreamFeedState.Success(
                    dreams = state.dreams,
                    temporallySort = false,
                    sortConfig = SortConfig(mode = SortMode.None)
                ),
                dreamCallback = object : DreamCallback {

                    override fun onClick(dream: Dream) {
                        onDreamClick(dream)
                    }

                    override fun onFavouriteClick(dream: Dream) {
                        onEvent(SearchEvent.DreamFavouriteClick(dream))
                    }

                }
            )
        }

        SearchTabs.Persons -> {
            PersonFeed(
                modifier = modifier,
                state = PersonFeedState.Success(
                    persons = state.persons
                ),
                personCallback = object : PersonCallback {
                    override fun onClick(person: Person) {
                        onPersonClick(person)
                    }

                    override fun onFavouriteClick(person: Person) {
                        onEvent(SearchEvent.PersonFavouriteClick(person))
                    }
                }
            )
        }

        SearchTabs.Locations -> {
            LocationFeed(
                modifier = modifier,
                state = LocationFeedState.Success(
                    locations = state.locations
                ),
                onLocationClick = {
                    onLocationClick(it)
                }
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    modifier: Modifier = Modifier,
    text: String,
    icon: Icon
) {
    val textStyle = TextStyle(
        fontSize = 14.sp,
        color = OneUITheme.colors.seslSecondaryTextColor,
        textAlign = TextAlign.Center
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconView(
            modifier = Modifier
                .size(64.dp),
            icon = icon
        )
        Text(
            text = text,
            style = textStyle
        )
    }
}