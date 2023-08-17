package com.snow.feature.dreams.screen.list;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.data.repository.DreamRepository
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortDirection
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.feed.DreamFeedState
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
    val dreamRepo: DreamRepository
) : ViewModel() {

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
        repo = dreamRepo,
        sortConfig = sortConfig
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DreamFeedState.Loading
    )

    fun onMenuClick() = viewModelScope.launch {
        _showMenu.emit(
            !showMenu.value
        )
    }

    fun onDreamFavouriteClick(dream: Dream) = viewModelScope.launch {
        dreamRepo
            .upsertDream(
                dream
                    .copy(
                        isFavourite = !dream.isFavourite
                    )
            )
    }

    fun onSortChange(sort: SortConfig) = viewModelScope.launch {
        _sortConfig.emit(
            sort
        )
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
private fun dreamListState(
    repo: DreamRepository,
    sortConfig: Flow<SortConfig>
): Flow<DreamFeedState> = sortConfig
    .flatMapMerge { sort ->
        repo
            .getAllDreams(sort)
            .map { Pair(sort, it) }
    }.map { tuple ->
        if (tuple.second.isEmpty()) DreamFeedState.Empty
        else DreamFeedState
            .Success(
                dreams = tuple.second,
                temporallySort = true,
                sortConfig = tuple.first
            )
    }

