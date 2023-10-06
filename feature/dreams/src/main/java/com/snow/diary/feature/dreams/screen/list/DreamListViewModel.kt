package com.snow.diary.feature.dreams.screen.list

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.dream.UpdateDream
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.feed.DreamFeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DreamListViewModel @Inject constructor(
    allDreams: AllDreams,
    val updateDream: UpdateDream
) : EventViewModel<DreamListEvent>() {

    private val _showMenu = MutableStateFlow(false)
    val showMenu: StateFlow<Boolean> = _showMenu

    private val _sortConfig = MutableStateFlow(
        SortConfig(
            mode = SortMode.Created,
            direction = SortDirection.Descending
        )
    )
    val sortConfig: StateFlow<SortConfig> = _sortConfig

    val dreamListState: StateFlow<DreamFeedState> = dreamListState(
        allDreams = allDreams,
        sortConfig = sortConfig
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DreamFeedState.Loading
    )

    override suspend fun handleEvent(event: DreamListEvent) = when (event) {
        is DreamListEvent.DreamFavouriteClick -> handleDreamFavouriteClick(event.dream)
        DreamListEvent.MenuClick -> handleMenuClick()
        is DreamListEvent.SortChange -> handleSortChange(event.sortConfig)
    }

    private fun handleMenuClick() = viewModelScope.launch {
        _showMenu.emit(
            !showMenu.value
        )
    }

    private fun handleDreamFavouriteClick(dream: Dream) = viewModelScope.launchInBackground {
        updateDream(listOf(dream))
    }

    private fun handleSortChange(sort: SortConfig) = viewModelScope.launch {
        _sortConfig.emit(
            sort
        )
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
private fun dreamListState(
    allDreams: AllDreams,
    sortConfig: StateFlow<SortConfig>
): Flow<DreamFeedState> = sortConfig
    .flatMapMerge { sort ->
        allDreams(
            AllDreams.Input(sort)
        )
    }.map { dreams ->
        if (dreams.isEmpty()) DreamFeedState.Empty
        else DreamFeedState
            .Success(
                dreams = dreams,
                temporallySort = sortConfig.value.allowsForTemporalSort,
                sortConfig = sortConfig.value
            )
    }

private val SortConfig.allowsForTemporalSort
    get() = mode in listOf(SortMode.Created, SortMode.Updated) &&
            direction == SortDirection.Descending

