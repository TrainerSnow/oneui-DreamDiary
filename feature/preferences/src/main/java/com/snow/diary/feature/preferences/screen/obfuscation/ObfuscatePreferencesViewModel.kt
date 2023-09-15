package com.snow.diary.feature.preferences.screen.obfuscation;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.domain.action.preferences.UpdateObfuscationPreferences
import com.snow.diary.core.domain.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ObfuscatePreferencesViewModel @Inject constructor(
    getPreferences: GetPreferences,
    val updateObfuscationPreferences: UpdateObfuscationPreferences
) : EventViewModel<ObfuscationPreferencesEvent>() {

    val preferences = getPreferences(Unit)
        .map { it.obfuscationPreferences }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    override suspend fun handleEvent(event: ObfuscationPreferencesEvent) =
        withContext(Dispatchers.IO) {

            val prefs = preferences.value ?: return@withContext

            when (event) {
                is ObfuscationPreferencesEvent.ChangeDoObfuscation -> updateObfuscationPreferences(
                    prefs.copy(obfuscationEnabled = event.enabled)
                )
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