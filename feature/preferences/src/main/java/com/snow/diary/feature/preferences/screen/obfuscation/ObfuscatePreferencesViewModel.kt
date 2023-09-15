package com.snow.diary.feature.preferences.screen.obfuscation;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.obfuscation.Deobfuscate
import com.snow.diary.core.domain.action.obfuscation.Obfuscate
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.domain.action.preferences.UpdateObfuscationPreferences
import com.snow.diary.core.domain.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ObfuscatePreferencesViewModel @Inject constructor(
    getPreferences: GetPreferences,
    val updateObfuscationPreferences: UpdateObfuscationPreferences,
    val obfuscate: Obfuscate,
    val deobfuscate: Deobfuscate
) : EventViewModel<ObfuscationPreferencesEvent>() {

    val preferences = getPreferences(Unit)
        .map { it.obfuscationPreferences }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _isObfuscating = MutableStateFlow(false)
    val isObfuscating: StateFlow<Boolean> = _isObfuscating

    override suspend fun handleEvent(event: ObfuscationPreferencesEvent) =
        withContext(Dispatchers.IO) {
            val prefs = preferences.value ?: return@withContext

            when (event) {
                is ObfuscationPreferencesEvent.ChangeDoObfuscation -> {
                    val newPrefs = prefs.copy(obfuscationEnabled = event.enabled)
                    updateObfuscationPreferences(newPrefs)
                    _isObfuscating.emit(true)
                    if (event.enabled) {
                        obfuscate(newPrefs)
                    }
                    else {
                        deobfuscate(newPrefs)
                    }
                    _isObfuscating.emit(false)
                }

                is ObfuscationPreferencesEvent.ChangeObfuscateDreams -> updateObfuscationPreferences(
                    prefs.copy(obfuscateDreams = event.obfuscate)
                )

                is ObfuscationPreferencesEvent.ChangeObfuscateLocations -> updateObfuscationPreferences(
                    prefs.copy(obfuscateLocations = event.obfuscate)
                )

                is ObfuscationPreferencesEvent.ChangeObfuscatePersons -> updateObfuscationPreferences(
                    prefs.copy(obfuscatePersons = event.obfuscate)
                )

                is ObfuscationPreferencesEvent.ChangeObfuscateRelations -> updateObfuscationPreferences(
                    prefs.copy(obfuscateRelations = event.obfuscate)
                )
            }
        }

}