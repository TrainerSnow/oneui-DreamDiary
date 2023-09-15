package com.snow.diary.feature.preferences.localization

import androidx.annotation.StringRes
import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.feature.preferences.R

val ColorMode.localizedName: Int
    @StringRes get() = when (this) {
        ColorMode.Light -> R.string.colormode_light
        ColorMode.Dark -> R.string.colormode_dark
        ColorMode.System -> R.string.colormode_system
    }