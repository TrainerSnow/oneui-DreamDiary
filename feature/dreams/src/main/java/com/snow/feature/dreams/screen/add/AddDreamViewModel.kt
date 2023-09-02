package com.snow.feature.dreams.screen.add;

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.Validator
import com.snow.diary.common.launchInBackground
import com.snow.diary.common.search.Search.filterSearch
import com.snow.diary.domain.action.cross.AddDreamLocationCrossref
import com.snow.diary.domain.action.cross.AddDreamPersonCrossref
import com.snow.diary.domain.action.cross.RemoveDreamLocationCrossref
import com.snow.diary.domain.action.cross.RemoveDreamPersonCrossref
import com.snow.diary.domain.action.dream.AddDreamAction
import com.snow.diary.domain.action.dream.DreamInformation
import com.snow.diary.domain.action.dream.UpdateDream
import com.snow.diary.domain.action.location.AllLocations
import com.snow.diary.domain.action.person.AllPersons
import com.snow.diary.domain.viewmodel.EventViewModel
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.rules.Rules
import com.snow.feature.dreams.nav.AddDreamArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class AddDreamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val dreamInformation: DreamInformation,
    val allPersons: AllPersons,
    val allLocations: AllLocations,
    val updateDream: UpdateDream,
    val addDream: AddDreamAction,
    val addDreamLocationCrossref: AddDreamLocationCrossref,
    val addDreamPersonCrossref: AddDreamPersonCrossref,
    val removeDreamLocationCrossref: RemoveDreamLocationCrossref,
    val removeDreamPersonCrossref: RemoveDreamPersonCrossref,
    @ApplicationContext val context: Context
) : EventViewModel<AddDreamEvent>() {

    private val args = AddDreamArgs(savedStateHandle)
    val isEdit = args.dreamId != null

    private val _inputState = MutableStateFlow(AddDreamInputState())
    val inputState = _inputState.asStateFlow()

    private val _extrasState = MutableStateFlow(AddDreamExtrasState())
    val extrasState = _extrasState.asStateFlow()

    private val _queryState = MutableStateFlow(AddDreamQueryState())
    val queryState = _queryState.asStateFlow()

    private val _uiState = MutableStateFlow(AddDreamUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (isEdit) {
            viewModelScope.launchInBackground {
                val dream = dreamInformation(args.dreamId!!)
                    .last()!!

                _inputState.emit(
                    inputState.value.run {
                        copy(
                            description = description.copy(dream.dream.description),
                            note = note.copy(dream.dream.note ?: ""),
                            markAsFavourite = dream.dream.isFavourite,
                            happiness = dream.dream.happiness,
                            clearness = dream.dream.clearness
                        )
                    }
                )
                _extrasState.emit(
                    extrasState.value.copy(
                        persons = dream.persons.map(PersonWithRelation::person),
                        locations = dream.locations
                    )
                )
            }
        }
    }

    private var personJob: Job? = null
    private var locationJob: Job? = null

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override suspend fun handleEvent(event: AddDreamEvent) = when (event) {
        is AddDreamEvent.ChangeClearness -> changeClearness(event.clearness)
        is AddDreamEvent.ChangeDescription -> changeDescription(event.description)
        is AddDreamEvent.ChangeHappiness -> changeHappiness(event.happiness)
        is AddDreamEvent.ChangeLocationQuery -> changeLocationQuery(event.query)
        is AddDreamEvent.ChangeMarkAsFavourite -> changeMarkAsFavourite(event.markAsFavourite)
        is AddDreamEvent.ChangeNote -> changeNote(event.note)
        is AddDreamEvent.ChangePersonQuery -> changePersonQuery(event.query)
        is AddDreamEvent.RemoveLocation -> removeLocation(event.location)
        is AddDreamEvent.RemovePerson -> removePerson(event.person)
        is AddDreamEvent.SelectLocation -> selectLocation(event.location)
        is AddDreamEvent.SelectPerson -> selectPerson(event.person)
    }

    private fun changeDescription(desc: String) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                description = inputState.value.description.copy(
                    input = desc
                )
            )
        )
    }

    private fun changeNote(note: String) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                note = inputState.value.note.copy(
                    input = note
                )
            )
        )
    }

    private fun changeMarkAsFavourite(markAsFavourite: Boolean) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                markAsFavourite = markAsFavourite
            )
        )
    }

    private fun changeHappiness(happiness: Float) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                happiness = happiness
            )
        )
    }

    private fun changeClearness(clearness: Float) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                clearness = clearness
            )
        )
    }

    private fun changePersonQuery(personQuery: String) = with(viewModelScope) {
        launch {
            _inputState.emit(
                inputState.value.copy(
                    personQuery = inputState.value.personQuery.copy(
                        input = personQuery
                    )
                )
            )
        }

        personJob?.cancel()
        personJob = launchInBackground {
            val persons = allPersons(AllPersons.Input())
                .lastOrNull()
                ?.filterSearch(personQuery) ?: emptyList()

            val showPopup = persons.isNotEmpty()

            _queryState.emit(
                queryState.value.copy(
                    persons = persons
                )
            )
            togglePersonPopupVisibility(showPopup)
        }
    }

    private fun changeLocationQuery(locationQuery: String) = with(viewModelScope) {
        launch {
            _inputState.emit(
                inputState.value.copy(
                    locationQuery = inputState.value.locationQuery.copy(
                        input = locationQuery
                    )
                )
            )
        }

        locationJob?.cancel()
        locationJob = launchInBackground {
            val locations = allLocations(AllLocations.Input())
                .lastOrNull()
                ?.filterSearch(locationQuery) ?: emptyList()

            val showPopup = locations.isNotEmpty()

            _queryState.emit(
                queryState.value.copy(
                    locations = locations
                )
            )
            toggleLocationPopupVisibility(showPopup)
        }
    }

    private fun selectPerson(person: Person) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                persons = extrasState.value.persons + person
            )
        )
        togglePersonPopupVisibility(false)
    }

    private fun removePerson(person: Person) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                persons = extrasState.value.persons - person
            )
        )
    }

    private fun selectLocation(location: Location) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                locations = extrasState.value.locations + location
            )
        )
        toggleLocationPopupVisibility(false)
    }

    private fun removeLocation(location: Location) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                locations = extrasState.value.locations - location
            )
        )
    }

    //TODO: Maybe move this whole thing (atleast update logic) to usecase
    private fun addDream() {

        var isOk = true
        //Form validation
        viewModelScope.launch {
            _inputState.emit(
                inputState.value.copy(
                    description = Validator.validate(
                        Rules.DreamContent,
                        context,
                        inputState.value.description.input
                    )
                )
            )
            isOk = inputState.value.description.error == null
        }

        if (isOk) return

        viewModelScope.launchInBackground {
            val dream = with(inputState.value) {
                Dream(
                    description = description.input,
                    note = note.input,
                    isFavourite = markAsFavourite,
                    created = LocalDate.now(),
                    updated = LocalDate.now(),
                    clearness = clearness,
                    happiness = happiness
                )
            }

            val id = if (isEdit) {
                updateDream(listOf(dream))
                args.dreamId!!
            } else addDream(listOf(dream))
                .first()

            if (!isEdit) {
                extrasState.value.persons.forEach { person ->
                    addDreamPersonCrossref(AddDreamPersonCrossref.Input(id, person.id!!))
                }
                extrasState.value.locations.forEach { location ->
                    addDreamLocationCrossref(AddDreamLocationCrossref.Input(id, location.id!!))
                }
            } else {
                val dreamInfo = dreamInformation(dream.id!!)
                    .first()!!

                val persons = dreamInfo.persons.map(PersonWithRelation::person)
                val locations = dreamInfo.locations

                val newPersons = extrasState.value.persons - persons.toSet()
                val newLocations = extrasState.value.locations - locations.toSet()

                val toRemovePersons = persons - extrasState.value.persons.toSet()
                val toRemoveLocations = locations - extrasState.value.locations.toSet()

                toRemovePersons.forEach {
                    removeDreamPersonCrossref(RemoveDreamPersonCrossref.Input(id, it.id!!))
                }
                toRemoveLocations.forEach {
                    removeDreamLocationCrossref(RemoveDreamLocationCrossref.Input(id, it.id!!))
                }

                newPersons.forEach {
                    addDreamPersonCrossref(AddDreamPersonCrossref.Input(id, it.id!!))
                }
                newLocations.forEach {
                    addDreamLocationCrossref(AddDreamLocationCrossref.Input(id, it.id!!))
                }
            }
        }
    }

    private fun togglePersonPopupVisibility(
        show: Boolean = _uiState.value.showPersonsPopup
    ) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                showPersonsPopup = show
            )
        )
    }

    private fun toggleLocationPopupVisibility(
        show: Boolean = _uiState.value.showLocationsPopup
    ) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                showLocationsPopup = show
            )
        )
    }

}