package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.preferences.ObfuscationPreferences

class UpdateObfuscationPreferences(
    private val prefsDataSource: PreferencesDataSource
): Action<ObfuscationPreferences, Unit>() {
    override suspend fun ObfuscationPreferences.compose() = let {
        prefsDataSource
            .updateObfuscationPrefs(this)
        Unit
    }

}