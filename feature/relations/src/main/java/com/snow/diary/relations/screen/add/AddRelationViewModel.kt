package com.snow.diary.relations.screen.add;

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.TextInput
import com.snow.diary.common.launchInBackground
import com.snow.diary.domain.action.relation.AddRelation
import com.snow.diary.domain.action.relation.RelationById
import com.snow.diary.domain.action.relation.UpdateRelation
import com.snow.diary.domain.viewmodel.EventViewModel
import com.snow.diary.model.data.Relation
import com.snow.diary.relations.nav.AddRelationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddRelationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    relationById: RelationById,
    val addRelation: AddRelation,
    val updateRelation: UpdateRelation,
) : EventViewModel<AddRelationEvent>() {

    private val args = AddRelationArgs(savedStateHandle)

    val isEdit = args.relationId != null

    private val _inputState = MutableStateFlow(AddRelationInputState())
    val inputState: StateFlow<AddRelationInputState> = _inputState

    private val _uiState = MutableStateFlow(AddRelationUiState())
    val uiState: StateFlow<AddRelationUiState> = _uiState

    init {
        if (isEdit) {
            viewModelScope.launchInBackground {
                val relation = relationById(args.relationId!!)
                    .first() ?: return@launchInBackground

                _inputState.emit(
                    AddRelationInputState(
                        name = TextInput(relation.name),
                        note = TextInput(relation.notes ?: ""),
                        color = Color(relation.color)
                    )
                )
            }
        }
    }


    override suspend fun handleEvent(event: AddRelationEvent) = when (event) {
        is AddRelationEvent.ChangeName -> changeName(event.name)
        is AddRelationEvent.ChangeNote -> changeNote(event.note)
        AddRelationEvent.Save -> save()
        is AddRelationEvent.ChangeColor -> changeColor(event.color)
        AddRelationEvent.ToggleColorPopupVisibility -> toggleColorPopupVisibility()
    }

    private fun toggleColorPopupVisibility() = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                showColorPickerPopup = !uiState.value.showColorPickerPopup
            )
        )
    }

    private fun changeColor(color: Color) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                color = color
            )
        )
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
        val relation = Relation(
            id = args.relationId,
            name = inputState.value.name.input,
            notes = inputState.value.note.input.ifBlank { null },
            color = inputState.value.color.toArgb()
        )

        if (isEdit)
            updateRelation(relation)
        else
            addRelation(relation)
    }

}