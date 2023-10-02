package com.snow.diary.feature.preferences.screen.obfuscation

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.obfuscation.Deobfuscate
import com.snow.diary.core.domain.action.obfuscation.Obfuscate
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.domain.action.preferences.UpdateObfuscationPreferences
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.preferences.ObfuscationPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    override suspend fun handleEvent(event: ObfuscationPreferencesEvent) = viewModelScope.launch {
        val oldPrefs = preferences.value ?: return@launch

        val newPrefs = when (event) {
            is ObfuscationPreferencesEvent.ChangeDoObfuscation -> oldPrefs.copy(obfuscationEnabled = event.enabled)
            is ObfuscationPreferencesEvent.ChangeObfuscateDreams -> oldPrefs.copy(obfuscateDreams = event.obfuscate)
            is ObfuscationPreferencesEvent.ChangeObfuscateLocations -> oldPrefs.copy(obfuscateLocations = event.obfuscate)
            is ObfuscationPreferencesEvent.ChangeObfuscatePersons -> oldPrefs.copy(obfuscatePersons = event.obfuscate)
            is ObfuscationPreferencesEvent.ChangeObfuscateRelations -> oldPrefs.copy(obfuscateRelations = event.obfuscate)
        }

        prefsChanged(newPrefs, oldPrefs)
    }


    //Takes in the new prefs. Prefs in storage and ui have not yet been adjusted.
    private suspend fun prefsChanged(
        newPrefs: ObfuscationPreferences,
        oldPrefs: ObfuscationPreferences
    ) = withContext(Dispatchers.IO) {
        _isObfuscating.emit(true)
        updateObfuscationPreferences(newPrefs)
        if (
            !newPrefs.obfuscationEnabled &&
            oldPrefs.obfuscationEnabled
        ) { //User changed obfuscation to off and did not change any other configs
            deobfuscate(Unit)
        } else if (
            newPrefs.obfuscationEnabled &&
            !oldPrefs.obfuscationEnabled
        ) { //User changed obfuscation to on and did not change any other configs
            obfuscate(newPrefs)
        } else { //User did not change obfuscation but did change other configs
            assert(newPrefs.obfuscationEnabled && oldPrefs.obfuscationEnabled) //Should be true because user can only change config when obfuscation is enabled
            //We deobfuscate first
            deobfuscate(Unit)
            //We obfuscate again, with the new prefs
            obfuscate(newPrefs)
        }

        _isObfuscating.emit(false)
    }

}