package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action

class UpdateBackupEnabled(
    private val prefsDataSource: PreferencesDataSource
) : Action<Boolean, Unit>() {
    override suspend fun Boolean.compose() = let {
        prefsDataSource
            .updateBackupEnabled(this)
        Unit
    }

}