package com.snow.diary.feature.relations.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.person.PersonsFromRelation
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.action.relation.DeleteRelation
import com.snow.diary.core.domain.action.relation.RelationById
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.data.Person
import com.snow.diary.feature.relations.nav.RelationDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RelationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    relationById: RelationById,
    personsFromRelation: PersonsFromRelation,
    val updatePerson: UpdatePerson,
    val deleteRelation: DeleteRelation
) : EventViewModel<RelationDetailEvent>() {

    private val args = RelationDetailArgs(savedStateHandle)

    val state = relationDetailState(relationById, args.relationId, personsFromRelation)
        .stateIn(
            scope = viewModelScope,
            initialValue = RelationDetailState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private val _tabs = MutableStateFlow(RelationDetailTab.General)
    val tabs: StateFlow<RelationDetailTab> = _tabs
    override suspend fun handleEvent(event: RelationDetailEvent) = when (event) {
        is RelationDetailEvent.ChangeTab -> changeTab(event.tab)
        is RelationDetailEvent.PersonFavouriteClick -> personFavouriteClick(event.person)
        is RelationDetailEvent.Delete -> delete()
    }

    private fun changeTab(tab: RelationDetailTab) = viewModelScope.launch {
        _tabs.emit(tab)
    }

    private fun personFavouriteClick(person: Person) = viewModelScope.launchInBackground {
        updatePerson(
            person.copy(
                isFavourite = !person.isFavourite
            )
        )
    }

    private fun delete() = viewModelScope.launchInBackground {
        (state.value as? RelationDetailState.Success)?.let {
            deleteRelation(it.relation)
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
private fun relationDetailState(
    relationById: RelationById,
    id: Long,
    personsFromRelation: PersonsFromRelation
): Flow<RelationDetailState> = relationById(id)
    .flatMapMerge { relation ->
        if (relation == null) flowOf(RelationDetailState.Error(id = id))
        else personsFromRelation(relation).map { persons ->
            RelationDetailState.Success(
                relation,
                persons
                    ?.map {
                        PersonWithRelations(it, listOf(relation)) //We act like the currently shown relation is the only one.
                    }.orEmpty()
            )
        }
    }