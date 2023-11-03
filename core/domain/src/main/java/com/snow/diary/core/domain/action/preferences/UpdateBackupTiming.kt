package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.preferences.BackupTiming

class UpdateBackupTiming(
    private val prefsDataSource: PreferencesDataSource
) : Action<BackupTiming, Unit>() {
    override suspend fun BackupTiming.compose() = let {
        prefsDataSource
            .updateBackupTiming(this)
        Unit
    }

}