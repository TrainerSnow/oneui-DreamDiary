package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.preferences.SecurityMode

class UpdateSecurityMode(
    private val prefsDataSource: PreferencesDataSource
): Action<SecurityMode, Unit>() {
    override suspend fun SecurityMode.compose() = let {
        prefsDataSource
            .updateSecurityMode(this)
        Unit
    }

}