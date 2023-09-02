package com.snow.feature.dreams.screen.list;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.common.launchInBackground
import com.snow.diary.domain.action.dream.AllDreams
import com.snow.diary.domain.action.dream.UpdateDream
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
    allDreams: AllDreams,
    val updateDream: UpdateDream
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
        allDreams = allDreams,
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

    fun onDreamFavouriteClick(dream: Dream) = viewModelScope.launchInBackground {
        updateDream(listOf(dream))
    }

    fun onSortChange(sort: SortConfig) = viewModelScope.launch {
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
                temporallySort = true,
                sortConfig = sortConfig.value
            )
    }

