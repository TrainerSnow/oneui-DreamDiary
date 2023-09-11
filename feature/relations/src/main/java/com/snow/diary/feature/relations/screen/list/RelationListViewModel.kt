package com.snow.diary.feature.relations.screen.list;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.relation.AllRelations
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.feed.RelationFeedState
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
internal class RelationListViewModel @Inject constructor(
    allRelations: AllRelations
) : EventViewModel<RelationsListEvent>() {

    private val _sortConfig = MutableStateFlow(
        SortConfig(
            mode = SortMode.Created,
            direction = SortDirection.Descending
        )
    )
    val sortConfig: StateFlow<SortConfig> = _sortConfig


    val feedState = relationListState(
        allRelations = allRelations,
        sortConfig = sortConfig
    ).stateIn(
        scope = viewModelScope,
        initialValue = RelationFeedState.Loading,
        started = SharingStarted.WhileSubscribed(5000L)
    )

    override suspend fun handleEvent(event: RelationsListEvent) = when (event) {
        is RelationsListEvent.SortChange -> handleSortChange(event.sortConfig)
    }

    private fun handleSortChange(sortConfig: SortConfig) = viewModelScope.launch {
        _sortConfig.emit(
            sortConfig
        )
    }


}

@OptIn(ExperimentalCoroutinesApi::class)
private fun relationListState(
    allRelations: AllRelations,
    sortConfig: StateFlow<SortConfig>
): Flow<RelationFeedState> = sortConfig
    .flatMapMerge { sort ->
        allRelations(
            AllRelations.Input(sort)
        )
    }.map { relations ->
        if (relations.isEmpty()) RelationFeedState.Empty
        else RelationFeedState.Success(relations)
    }