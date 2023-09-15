package com.snow.diary.feature.preferences.screen.obfuscation

sealed class ObfuscationPreferencesEvent {

    data class ChangeDoObfuscation(
        val enabled: Boolean
    ): ObfuscationPreferencesEvent()

    data class ChangeObfuscateDreams(
        val obfuscate: Boolean
    ): ObfuscationPreferencesEvent()

    data class ChangeObfuscatePersons(
        val obfuscate: Boolean
    ): ObfuscationPreferencesEvent()

    data class ChangeObfuscateLocations(
        val obfuscate: Boolean
    ): ObfuscationPreferencesEvent()

    data class ChangeObfuscateRelations(
        val obfuscate: Boolean
    ): ObfuscationPreferencesEvent()

}
