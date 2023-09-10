package com.snow.diary.locations.screen.add;

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.TextInput
import com.snow.diary.common.launchInBackground
import com.snow.diary.domain.action.location.AddLocation
import com.snow.diary.domain.action.location.LocationById
import com.snow.diary.domain.action.location.UpdateLocation
import com.snow.diary.domain.viewmodel.EventViewModel
import com.snow.diary.locations.nav.AddLocationArgs
import com.snow.diary.model.data.Coordinates
import com.snow.diary.model.data.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddPersonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    locationById: LocationById,
    val addLocation: AddLocation,
    val updateLocation: UpdateLocation,
) : EventViewModel<AddLocationEvent>() {

    private val args = AddLocationArgs(savedStateHandle)

    val isEdit = args.locationId != null

    private val _inputState = MutableStateFlow(AddLocationState())
    val inputState: StateFlow<AddLocationState> = _inputState

    init {
        if (isEdit) {
            viewModelScope.launchInBackground {
                val location = locationById(args.locationId!!)
                    .first() ?: return@launchInBackground

                _inputState.emit(
                    AddLocationState(
                        name = TextInput(location.name),
                        note = TextInput(location.notes),
                    )
                )
            }
        }
    }


    override suspend fun handleEvent(event: AddLocationEvent) = when (event) {
        is AddLocationEvent.ChangeName -> changeName(event.name)
        is AddLocationEvent.ChangeNote -> changeNote(event.note)
        AddLocationEvent.Save -> save()
    }

    private fun changeName(name: String) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                name = inputState.value.name.copy(
                    input = name
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

    private fun save() = viewModelScope.launchInBackground {
        //TODO: This only saves the **first** of the relations added.
        val location = Location(
            id = args.locationId,
            name = inputState.value.name.input,
            notes = inputState.value.note.input,
            coordinates = Coordinates(0F, 0F) //TODO: Change when using google maps api
        )

        if (isEdit)
            updateLocation(location)
        else
            addLocation(location)
    }

}