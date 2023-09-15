package com.snow.diary.feature.preferences.localization

import androidx.annotation.StringRes
import com.snow.diary.core.model.preferences.SecurityMode
import com.snow.diary.feature.preferences.R

val SecurityMode.localizedName: Int
    @StringRes get() = when (this) {
    SecurityMode.Biometric -> R.string.securitymode_biometric
    SecurityMode.Default -> R.string.securitymode_default
    SecurityMode.None -> R.string.securitymode_none
}