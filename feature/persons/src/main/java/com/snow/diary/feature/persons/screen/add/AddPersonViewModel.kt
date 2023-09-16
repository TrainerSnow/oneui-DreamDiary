package com.snow.diary.feature.persons.screen.add;

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.common.search.Search.filterSearch
import com.snow.diary.core.domain.action.cross.person_relation.AddPersonRelationCrossref
import com.snow.diary.core.domain.action.cross.person_relation.AllPersonRelationCrossrefs
import com.snow.diary.core.domain.action.cross.person_relation.RemovePersonRelationCrossref
import com.snow.diary.core.domain.action.person.AddPerson
import com.snow.diary.core.domain.action.person.PersonFromId
import com.snow.diary.core.domain.action.person.PersonWithRelationsAct
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.action.relation.AllRelations
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.form.TextInput
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.persons.nav.AddPersonArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class AddPersonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val allRelations: AllRelations,
    personFromId: PersonFromId,
    personWithRelationsAct: PersonWithRelationsAct,
    val addPerson: AddPerson,
    val updatePerson: UpdatePerson,
    val addPersonRelationCrossref: AddPersonRelationCrossref,
    val deletePersonRelationCrossref: RemovePersonRelationCrossref,
    val allPersonRelationCrossrefs: AllPersonRelationCrossrefs
) : EventViewModel<AddPersonEvent>() {

    private val args = AddPersonArgs(savedStateHandle)

    val isEdit = args.personId != null

    private val _inputState = MutableStateFlow(AddPersonState())
    val inputState: StateFlow<AddPersonState> = _inputState

    private val _selectedRelations = MutableStateFlow<List<Relation>>(emptyList())
    val selectedRelations: StateFlow<List<Relation>> = _selectedRelations

    private val _selectableRelations = MutableStateFlow<List<Relation>>(emptyList())
    val selectableRelations: StateFlow<List<Relation>> = _selectableRelations

    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> = _showPopup


    init {
        if (isEdit) {
            viewModelScope.launchInBackground {
                val person = personFromId(args.personId!!)
                    .first() ?: return@launchInBackground

                val relations = personWithRelationsAct(person)
                    .firstOrNull()
                    ?.relation ?: emptyList()

                _inputState.emit(
                    AddPersonState(
                        name = TextInput(person.name),
                        note = TextInput(person.notes ?: ""),
                        markAsFavourite = person.isFavourite
                    )
                )
                _selectedRelations.emit(relations)
            }
        }
    }


    override suspend fun handleEvent(event: AddPersonEvent) = when (event) {
        is AddPersonEvent.ChangeMarkAsFavourite -> changeMarkAsFavourite()
        is AddPersonEvent.ChangeName -> changeName(event.name)
        is AddPersonEvent.ChangeNote -> changeNote(event.note)
        is AddPersonEvent.ChangeRelationQUery -> changeRelationQuery(event.query)
        AddPersonEvent.Save -> save()
        is AddPersonEvent.SelectRelation -> selectRelation(event.relation)
        AddPersonEvent.ToggleRelationsPopup -> togglePopup()
        is AddPersonEvent.UnselectRelation -> unselectRelation(event.relation)
    }

    private fun changeMarkAsFavourite() = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                markAsFavourite = !inputState.value.markAsFavourite
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

    private fun changeRelationQuery(query: String) = viewModelScope.launch {
        _inputState.emit(
            inputState.value.copy(
                relationQuery = query
            )
        )
        withContext(Dispatchers.IO) {
            val relations = allRelations(AllRelations.Input())
                .first() - selectedRelations.value.toSet()

            val matching = relations
                .filterSearch(query)

            _selectableRelations.emit(matching)
            togglePopup(matching.isNotEmpty())
        }
    }

    private fun save() = viewModelScope.launchInBackground {
        val relations = selectedRelations.value

        val person = Person(
            id = args.personId,
            name = inputState.value.name.input,
            notes = inputState.value.note.input.ifBlank { null },
            isFavourite = inputState.value.markAsFavourite
        )

        val id = if (isEdit) {
            updatePerson(person)
            args.personId!!
        } else addPerson(person)

        val crossrefs = relations.map { relation ->
            Pair(id, relation.id!!)
        }
        if(!isEdit) {
            crossrefs.forEach { crossref ->
                addPersonRelationCrossref(crossref.toAddInput())
            }
        } else {
            val oldCrossrefs = allPersonRelationCrossrefs(Unit)
                .firstOrNull()
                ?.filter {
                    it.first == id
                } ?: emptyList()

            val toRemove = oldCrossrefs - crossrefs.toSet()
            val toAdd = crossrefs - oldCrossrefs.toSet()

            toRemove.forEach {
                deletePersonRelationCrossref(it.toRemoveInput())
            }
            toAdd.forEach {
                addPersonRelationCrossref(it.toAddInput())
            }
        }
    }

    private fun selectRelation(relation: Relation) = viewModelScope.launch {
        _selectedRelations.emit(
            selectedRelations.value + relation
        )
        togglePopup(false)
        changeRelationQuery("")
    }

    private fun unselectRelation(relation: Relation) = viewModelScope.launch {
        _selectedRelations.emit(
            selectedRelations.value - relation
        )
    }

    private fun togglePopup(show: Boolean = !showPopup.value) = viewModelScope.launch {
        _showPopup.emit(
            show
        )
    }

}

private fun Pair<Long, Long>.toRemoveInput() = RemovePersonRelationCrossref.Input(first, second)


private fun Pair<Long, Long>.toAddInput() = AddPersonRelationCrossref.Input(first, second)