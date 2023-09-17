package com.snow.diary.core.model.preferences;

data class UserPreferences(

    val colorMode: ColorMode,

    val requireAuth: Boolean,

    val obfuscationPreferences: ObfuscationPreferences

)