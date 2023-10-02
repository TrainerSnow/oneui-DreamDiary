package com.snow.diary.feature.dreams.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.dream.DeleteDream
import com.snow.diary.core.domain.action.dream.DreamInformation
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.feature.dreams.nav.DreamDetailArgs
import com.snow.diary.feature.dreams.screen.detail.component.DreamDetailEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DreamDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dreamInformation: DreamInformation,
    val updatePerson: UpdatePerson,
    val deleteDreamAct: DeleteDream
) : EventViewModel<DreamDetailEvent>() {

    private val args = DreamDetailArgs(savedStateHandle)

    val dreamDetailState = dreamInformation(args.dreamId)
        .map {
            if (it == null) DreamDetailState.Error(args.dreamId)
            else DreamDetailState.Success(
                it.dream, it.locations, it.persons
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DreamDetailState.Loading
        )

    private val _tabState = MutableStateFlow(
        DreamDetailTabState(
            tab = DreamDetailTab.General, subtab = DreamDetailSubtab.Content
        )
    )
    val tabState: StateFlow<DreamDetailTabState> = _tabState

    override suspend fun handleEvent(event: DreamDetailEvent): Any = when (event) {
        is DreamDetailEvent.ChangeTabState -> handleChangeTabState(event.tabState)
        is DreamDetailEvent.DeleteDream -> handleDeleteDream(event.dream)
        is DreamDetailEvent.PersonFavouriteClick -> handlePersonFavouriteClick(event.person)
    }

    private fun handlePersonFavouriteClick(person: Person) = viewModelScope.launchInBackground {
        updatePerson(
            person.copy(
                isFavourite = !person.isFavourite
            )
        )
    }

    private fun handleDeleteDream(dream: Dream) = viewModelScope.launchInBackground {
        //TODO: Give option to restore dream via global toast
        deleteDreamAct(dream)
    }

    private fun handleChangeTabState(tabState: DreamDetailTabState) =
        viewModelScope.launch { _tabState.emit(tabState) }

}