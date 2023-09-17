package com.snow.diary.feature.preferences.screen.main

import com.snow.diary.core.model.preferences.ColorMode

sealed class MainPreferencesEvent {

    data class ChangeColorMode(
        val mode: ColorMode
    ): MainPreferencesEvent()

    data class ChangeRequireAuth(
        val requireAuth: Boolean
    ): MainPreferencesEvent()

}
