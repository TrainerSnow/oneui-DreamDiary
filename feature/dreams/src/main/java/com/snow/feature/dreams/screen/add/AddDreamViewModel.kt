package com.snow.feature.dreams.screen.add;

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.Validator
import com.snow.diary.common.launchInBackground
import com.snow.diary.common.search.Search.filterSearch
import com.snow.diary.data.repository.DreamRepository
import com.snow.diary.data.repository.LocationRepository
import com.snow.diary.data.repository.PersonRepository
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
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class AddDreamViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val dreamRepo: DreamRepository,
    val personRepo: PersonRepository,
    val locationRepo: LocationRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    val args = AddDreamArgs(savedStateHandle)

    private val _inputState = MutableStateFlow(AddDreamInputState())
    val inputState = _inputState.asStateFlow()

    private val _extrasState = MutableStateFlow(AddDreamExtrasState())
    val extrasState = _extrasState.asStateFlow()

    private val _queryState = MutableStateFlow(AddDreamQueryState())
    val queryState = _queryState.asStateFlow()

    private val _uiState = MutableStateFlow(AddDreamUiState())
    val uiState = _uiState.asStateFlow()


    private var personJob: Job? = null
    private var locationJob: Job? = null

    fun changeDescription(desc: String) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                description = inputState.value.description.copy(
                    input = desc
                )
            )
        )
    }

    fun changeNote(note: String) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                note = inputState.value.note.copy(
                    input = note
                )
            )
        )
    }

    fun changeMarkAsFavourite(markAsFavourite: Boolean) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                markAsFavourite = markAsFavourite
            )
        )
    }

    fun changeHappiness(happiness: Float) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                happiness = happiness
            )
        )
    }

    fun changePersonQuery(personQuery: String) = with(viewModelScope) {
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
            val persons = personRepo
                .getAllPersons()
                .lastOrNull()
                ?.filterSearch(personQuery) ?: emptyList()

            val showPopup = persons.isNotEmpty()

            _uiState.emit(
                uiState.value.copy(
                    showPersonsPopup = showPopup
                )
            )
            toggleLocationPopupVisibility(true)
        }
    }

    fun changeLocationQuery(locationQuery: String) = with(viewModelScope) {
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
            val locations = locationRepo
                .getAllLocations()
                .lastOrNull()
                ?.filterSearch(locationQuery) ?: emptyList()

            val showPopup = locations.isNotEmpty()

            _queryState.emit(
                queryState.value.copy(
                    locations = locations
                )
            )
            toggleLocationPopupVisibility(true)
        }
    }

    fun selectPerson(person: Person) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                persons = extrasState.value.persons + person
            )
        )
        togglePersonPopupVisibility(false)
    }

    fun removePerson(person: Person) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                persons = extrasState.value.persons - person
            )
        )
    }

    fun selectLocation(location: Location) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                locations = extrasState.value.locations + location
            )
        )
        toggleLocationPopupVisibility(false)
    }

    fun removeLocation(location: Location) = viewModelScope.launch {
        _extrasState.emit(
            extrasState.value.copy(
                locations = extrasState.value.locations - location
            )
        )
    }

    fun addDream() {

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

            val id = dreamRepo
                .insert(dream)
                .first()

            extrasState.value.persons.forEach { person ->
                dreamRepo
                    .upsertDreamPersonCrossref(id, person.id!!)
            }
            extrasState.value.locations.forEach { location ->
                dreamRepo
                    .upsertDreamLocationCrossref(id, location.id!!)
            }
        }
    }

    fun togglePersonPopupVisibility(
        show: Boolean = _uiState.value.showPersonsPopup
    ) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                showPersonsPopup = show
            )
        )
    }

    fun toggleLocationPopupVisibility(
        show: Boolean = _uiState.value.showLocationsPopup
    ) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                showLocationsPopup = show
            )
        )
    }

}