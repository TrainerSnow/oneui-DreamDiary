package com.snow.diary.feature.search.screen;

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.common.search.Search.filterSearch
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.dream.UpdateDream
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.person.PersonWithRelationsAct
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.ui.component.StatisticsDateRanges
import com.snow.diary.feature.search.nav.SearchArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    allPersons: AllPersons,
    allDreams: AllDreams,
    allLocations: AllLocations,
    personWithRelations: PersonWithRelationsAct,
    val updateDream: UpdateDream,
    val updatePerson: UpdatePerson
) : EventViewModel<SearchEvent>() {

    private val args: SearchArgs = SearchArgs(savedStateHandle) //TODO: Implement

    private val _uiState = MutableStateFlow(
        SearchUiState(
            selectedTab = SearchTabs.entries.find { it.name == args.tabName } ?: SearchTabs.Dreams
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState

    private val dreams = uiState.map { it.range }.flatMapMerge { range ->
        allDreams(AllDreams.Input(dateRange = range.range))
    }
    private val persons = allPersons(AllPersons.Input()).flatMapMerge {
        val flows = it.map { personWithRelations(it) }
        combine(flows) { it.toList() }
    }
    private val locations = allLocations(AllLocations.Input())

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.NoQuery)
    val searchState: StateFlow<SearchState> = _searchState

    private var searchJob: Job? = null

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override suspend fun handleEvent(event: SearchEvent) = when (event) {
        is SearchEvent.ChangeQuery -> changeQuery(event.query)
        is SearchEvent.ChangeRange -> changeRange(event.range)
        is SearchEvent.ChangeTab -> changeTab(event.tab)
        is SearchEvent.DreamFavouriteClick -> markDreamAsFavourite(event.dream)
        is SearchEvent.PersonFavouriteClick -> markPersonAsFavourite(event.person)
        SearchEvent.ToggleRangeDialog -> toggleRangeDialog()
    }

    private fun toggleRangeDialog() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                showRangeDialog = !it.showRangeDialog
            )
        }
    }

    private fun markDreamAsFavourite(dream: Dream) = viewModelScope.launchInBackground {
        updateDream(
            listOf(
                dream.copy(
                    isFavourite = !dream.isFavourite
                )
            )
        )
    }

    private fun markPersonAsFavourite(person: Person) = viewModelScope.launchInBackground {
        updatePerson(
            person.copy(
                isFavourite = !person.isFavourite
            )
        )
    }

    private fun changeRange(range: StatisticsDateRanges) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                range = range
            )
        }
    }

    private fun changeQuery(query: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    query = query
                )
            }
        }


        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if(query.isBlank()) {
                _searchState.emit(SearchState.NoQuery)
                return@launch
            }

            _searchState.emit(SearchState.Searching)

            when (uiState.value.selectedTab) {
                SearchTabs.Dreams -> {
                    val matching = dreams
                        .stateIn(viewModelScope)
                        .value
                        .filterSearch(query)

                    _searchState.emit(
                        if (matching.isEmpty()) SearchState.NoResults
                        else SearchState.Success(matching, emptyList(), emptyList())
                    )
                }

                SearchTabs.Persons -> {
                    val persons = persons
                        .stateIn(viewModelScope)
                        .value

                    val matchingIds = persons
                        .map(PersonWithRelations::person)
                        .filterSearch(query)
                        .map(Person::id)

                    val matching = persons.filter { it.person.id in matchingIds }

                    _searchState.emit(
                        if (matching.isEmpty()) SearchState.NoResults
                        else SearchState.Success(emptyList(), matching, emptyList())
                    )
                }

                SearchTabs.Locations -> {
                    val matching = locations
                        .stateIn(viewModelScope)
                        .value
                        .filterSearch(query)

                    _searchState.emit(
                        if (matching.isEmpty()) SearchState.NoResults
                        else SearchState.Success(emptyList(), emptyList(), matching)
                    )
                }
            }
        }
    }

    private fun changeTab(tab: SearchTabs) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedTab = tab
                )
            }
        }
    }

}