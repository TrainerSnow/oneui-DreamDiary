package com.snow.diary.core.model.preferences;

data class UserPreferences(

    val colorMode: ColorMode,

    val securityMode: SecurityMode,

    val obfuscationPreferences: ObfuscationPreferences

)