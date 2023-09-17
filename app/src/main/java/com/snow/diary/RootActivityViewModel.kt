package com.snow.diary;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.model.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RootActivityViewModel @Inject constructor(
    getPreferences: GetPreferences
) : ViewModel() {

    val rootState: StateFlow<RootState> = getPreferences(Unit)
        .map { RootState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RootState.Loading
        )

}

sealed class RootState {

    data object Loading: RootState()

    data class Success(
        val preferences: UserPreferences
    ): RootState()

}