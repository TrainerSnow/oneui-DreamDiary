package com.snow.feature.dreams.screen.detail;

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.common.launchInBackground
import com.snow.diary.data.repository.DreamRepository
import com.snow.diary.data.repository.PersonRepository
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Person
import com.snow.feature.dreams.nav.DreamDetailArgs
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
    val dreamRepo: DreamRepository,
    val personRepo: PersonRepository
) : ViewModel() {

    private val args = DreamDetailArgs(savedStateHandle)

    val dreamDetailState = dreamRepo.getExtendedDreamById(args.dreamId).map { dream ->
        if (dream == null) DreamDetailState.Error(
            id = args.dreamId
        )
        else DreamDetailState.Success(
            dream = dream
        )
    }.stateIn(
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

    fun personFavouriteClick(person: Person) = viewModelScope.launchInBackground {
        personRepo.upsertPerson(
            person.copy(
                isFavourite = !person.isFavourite
            )
        )
    }

    fun deleteDream(dream: Dream) = viewModelScope.launchInBackground {
        //TODO: Give option to restore dream via global toast
        dreamRepo
            .deleteDream(dream)
    }

    fun changeTabState(tabState: DreamDetailTabState) =
        viewModelScope.launch { _tabState.emit(tabState) }

}