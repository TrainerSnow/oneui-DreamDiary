package com.snow.diary.feature.preferences.screen.main;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.domain.action.preferences.UpdateColorMode
import com.snow.diary.core.domain.action.preferences.UpdateObfuscationPreferences
import com.snow.diary.core.domain.action.preferences.UpdateSecurityMode
import com.snow.diary.core.domain.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainPreferencesViewModel @Inject constructor(
    getPreferences: GetPreferences,
    val updateColorMode: UpdateColorMode,
    val updateSecurityMode: UpdateSecurityMode,
    val updateObfuscationPreferences: UpdateObfuscationPreferences
) : EventViewModel<MainPreferencesEvent>() {

    val preferences = getPreferences(Unit)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    override suspend fun handleEvent(event: MainPreferencesEvent) = withContext(Dispatchers.IO) {
        when (event) {
            is MainPreferencesEvent.ChangeColorMode -> updateColorMode(event.mode)
            is MainPreferencesEvent.ChangeSecurityMode -> updateSecurityMode(event.mode)
        }
    }
}