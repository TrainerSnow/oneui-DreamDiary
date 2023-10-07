package com.snow.diary.feature.persons.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.dream.DreamsFromPerson
import com.snow.diary.core.domain.action.dream.UpdateDream
import com.snow.diary.core.domain.action.person.DeletePerson
import com.snow.diary.core.domain.action.person.PersonFromId
import com.snow.diary.core.domain.action.person.PersonWithRelationsAct
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.feature.persons.nav.PersonDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PersonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    personById: PersonFromId,
    personWithRelationsAct: PersonWithRelationsAct,
    dreamsFromPerson: DreamsFromPerson,
    val updateDream: UpdateDream,
    val deletePerson: DeletePerson
) : EventViewModel<PersonDetailEvent>() {

    private val args = PersonDetailArgs(savedStateHandle)

    val state =
        personDetailState(personById, args.personId, dreamsFromPerson, personWithRelationsAct)
            .stateIn(
                scope = viewModelScope,
                initialValue = PersonDetailState.Loading,
                started = SharingStarted.WhileSubscribed(5000)
            )

    private val _tabs = MutableStateFlow(PersonDetailTab.General)
    val tabs: StateFlow<PersonDetailTab> = _tabs
    override suspend fun handleEvent(event: PersonDetailEvent) = when (event) {
        is PersonDetailEvent.ChangeTab -> changeTab(event.tab)
        is PersonDetailEvent.DreamFavouriteClick -> dreamFavouriteClick(event.dream)
        is PersonDetailEvent.Delete -> delete()
    }

    private fun changeTab(tab: PersonDetailTab) = viewModelScope.launch {
        _tabs.emit(tab)
    }

    private fun dreamFavouriteClick(dream: Dream) = viewModelScope.launchInBackground {
        updateDream(
            listOf(
                dream.copy(
                    isFavourite = !dream.isFavourite
                )
            )
        )
    }

    private fun delete() = viewModelScope.launchInBackground {
        (state.value as? PersonDetailState.Success)?.let {
            deletePerson(it.person)
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
private fun personDetailState(
    personById: PersonFromId,
    id: Long,
    dreamsFromPerson: DreamsFromPerson,
    personWithRelationsAct: PersonWithRelationsAct
): Flow<PersonDetailState> = personById(id)
    .flatMapMerge { person ->
        if (person == null) return@flatMapMerge flowOf(PersonDetailState.Error(id = id))
        else combine(
            flow = dreamsFromPerson(person),
            flow2 = personWithRelationsAct(person)
        ) { dreams, pwr ->
            if (pwr == null) return@combine PersonDetailState.Error(id = id)
            PersonDetailState.Success(person, pwr.relation, dreams)
        }
    }