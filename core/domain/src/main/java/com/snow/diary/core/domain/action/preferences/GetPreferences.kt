package com.snow.diary.core.domain.action.preferences;

import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.model.preferences.UserPreferences

class GetPreferences(
    private val prefsDataSource: PreferencesDataSource
): FlowAction<Unit, UserPreferences>() {

    override fun Unit.createFlow() = prefsDataSource
        .data

}