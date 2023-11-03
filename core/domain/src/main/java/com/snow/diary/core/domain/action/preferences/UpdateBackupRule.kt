package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.preferences.BackupRule

class UpdateBackupRule(
    private val prefsDataSource: PreferencesDataSource
) : Action<BackupRule, Unit>() {
    override suspend fun BackupRule.compose() = let {
        prefsDataSource
            .updateBackupRule(this)
        Unit
    }

}