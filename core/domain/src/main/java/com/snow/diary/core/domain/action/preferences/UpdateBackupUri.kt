package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action

class UpdateBackupUri(
    private val prefsDataSource: PreferencesDataSource
) : Action<String, Unit>() {
    override suspend fun String.compose() = let {
        prefsDataSource
            .updateBackupUri(this)
        Unit
    }

}