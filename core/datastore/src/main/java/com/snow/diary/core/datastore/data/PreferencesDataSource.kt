package com.snow.diary.core.datastore.data

import androidx.datastore.core.DataStore
import com.snow.diary.core.datastore.ColorModeProto
import com.snow.diary.core.datastore.ObfuscationPreferencesProto
import com.snow.diary.core.datastore.UserPreferences
import com.snow.diary.core.datastore.copy
import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.core.model.preferences.ObfuscationPreferences
import kotlinx.coroutines.flow.map

typealias UserPreferencesModel = com.snow.diary.core.model.preferences.UserPreferences

class PreferencesDataSource(
    private val dataStore: DataStore<UserPreferences>
) {

    suspend fun updateColorMode(colorMode: ColorMode) = dataStore
        .updateData { prefs ->
            prefs.copy {
                this.colorMode = when (colorMode) {
                    ColorMode.Light -> ColorModeProto.COLOR_MODE_LIGHT
                    ColorMode.Dark -> ColorModeProto.COLOR_MODE_DARK
                    ColorMode.System -> ColorModeProto.COLOR_MODE_SYSTEM
                }
            }
        }

    suspend fun updateRequireAuth(requireAuth: Boolean) = dataStore
        .updateData { prefs ->
            prefs.copy {
                this.requireAuth = requireAuth
            }
        }

    suspend fun updateObfuscationPrefs(obfuscationPrefs: ObfuscationPreferences) = dataStore
        .updateData { prefs ->
            prefs.copy {
                this.obfuscationPreferences = with(obfuscationPrefs) {
                    ObfuscationPreferencesProto
                        .newBuilder()
                        .setObfuscationEnabled(obfuscationEnabled)
                        .setObfuscateDreams(obfuscateDreams)
                        .setObfuscatePersons(obfuscatePersons)
                        .setObfuscateLocations(obfuscateLocations)
                        .setObfuscateRelations(obfuscateRelations)
                        .build()
                }
            }
        }

    val data = dataStore
        .data
        .map {
            UserPreferencesModel(
                colorMode = when (it.colorMode) {
                    ColorModeProto.COLOR_MODE_DARK -> ColorMode.Dark
                    ColorModeProto.COLOR_MODE_LIGHT -> ColorMode.Light
                    else -> ColorMode.System
                },
                requireAuth = it.requireAuth,
                obfuscationPreferences = with(it.obfuscationPreferences) {
                    ObfuscationPreferences(
                        obfuscationEnabled,
                        obfuscateDreams,
                        obfuscatePersons,
                        obfuscateLocations,
                        obfuscateRelations
                    )
                }
            )
        }

}