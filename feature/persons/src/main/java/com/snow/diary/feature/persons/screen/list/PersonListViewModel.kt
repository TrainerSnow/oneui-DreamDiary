package com.snow.diary.feature.persons.screen.list;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.person.PersonWithRelationAct
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortDirection
import com.snow.diary.core.model.sort.SortMode
import com.snow.diary.core.ui.feed.PersonFeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PersonListViewModel @Inject constructor(
    allPersons: AllPersons,
    personWithRelationAct: PersonWithRelationAct,
    val updatePerson: UpdatePerson
) : EventViewModel<PersonListEvent>() {

    private val _sortConfig = MutableStateFlow(
        SortConfig(
            mode = SortMode.Created,
            direction = SortDirection.Descending
        )
    )
    val sortConfig: StateFlow<SortConfig> = _sortConfig


    val feedState = personListState(
        allPersons = allPersons,
        personWithRelationAct = personWithRelationAct,
        sortConfig = sortConfig
    ).stateIn(
        scope = viewModelScope,
        initialValue = PersonFeedState.Loading,
        started = SharingStarted.WhileSubscribed(5000L)
    )

    override suspend fun handleEvent(event: PersonListEvent) = when (event) {
        is PersonListEvent.PersonFavouriteClick -> handlePersonFavouriteClick(event.person)
        is PersonListEvent.SortChange -> handleSortChange(event.sortConfig)
    }

    private fun handlePersonFavouriteClick(person: Person) = viewModelScope.launchInBackground {
        updatePerson(
            person.copy(
                isFavourite = !person.isFavourite
            )
        )
    }

    private fun handleSortChange(sortConfig: SortConfig) = viewModelScope.launch {
        _sortConfig.emit(
            sortConfig
        )
    }


}

@OptIn(ExperimentalCoroutinesApi::class)
private fun personListState(
    allPersons: AllPersons,
    personWithRelationAct: PersonWithRelationAct,
    sortConfig: StateFlow<SortConfig>
): Flow<PersonFeedState> = sortConfig
    .flatMapMerge { sort ->
        allPersons(
            AllPersons.Input(sort)
        )
    }.flatMapMerge { persons ->
        if(persons.isEmpty()) flowOf(emptyList())
        else combine(
            flows = persons.map {
                personWithRelationAct(it)
            }
        ) {
            it.toList()
        }
    }
    .map {
        if (it.isEmpty()) PersonFeedState.Empty
        else PersonFeedState.Success(
            it,
            false,
            sortConfig.value
        )
    }