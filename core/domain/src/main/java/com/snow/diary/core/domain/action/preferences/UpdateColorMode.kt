package com.snow.diary.core.domain.action.preferences

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.preferences.ColorMode

class UpdateColorMode(
    private val prefsDataSource: PreferencesDataSource
): Action<ColorMode, Unit>() {
    override suspend fun ColorMode.compose() = let {
        prefsDataSource
            .updateColorMode(this)
        Unit
    }

}