package com.snow.diary.feature.preferences.screen.main

import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.core.model.preferences.SecurityMode

sealed class MainPreferencesEvent {

    data class ChangeColorMode(
        val mode: ColorMode
    ): MainPreferencesEvent()

    data class ChangeSecurityMode(
        val mode: SecurityMode
    ): MainPreferencesEvent()

}
