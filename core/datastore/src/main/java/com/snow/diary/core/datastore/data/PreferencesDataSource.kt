package com.snow.diary.core.datastore.data

import androidx.datastore.core.DataStore
import com.snow.diary.core.datastore.BackupTimingProto
import com.snow.diary.core.datastore.ColorModeProto
import com.snow.diary.core.datastore.ObfuscationPreferencesProto
import com.snow.diary.core.datastore.UserPreferences
import com.snow.diary.core.datastore.copy
import com.snow.diary.core.model.preferences.BackupPreferences
import com.snow.diary.core.model.preferences.BackupRule
import com.snow.diary.core.model.preferences.BackupTiming
import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.core.model.preferences.ObfuscationPreferences
import kotlinx.coroutines.flow.map
import java.net.URI
import java.time.Period

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

    suspend fun updateBackupEnabled(enabled: Boolean) = dataStore
        .updateData { prefs ->
            prefs.copy {
                backupPreferences = backupPreferences.copy {
                    backupEnabled = enabled
                }
            }
        }

    suspend fun updateBackupUri(uri: String) = dataStore
        .updateData { prefs ->
            prefs.copy {
                backupPreferences = backupPreferences.copy {
                    backupDirUri = uri
                }
            }
        }

    suspend fun updateBackupRule(backupRule: BackupRule) = dataStore
        .updateData { prefs ->
            prefs.copy {
                backupPreferences = backupPreferences.copy {
                    backupRuleInfinite = backupRule == BackupRule.Infinite
                    backupRuleDays = (backupRule as? BackupRule.TimeLimit)?.period?.days ?: -1
                    backupRuleMegabytes = (backupRule as? BackupRule.StorageLimit)?.megabytes ?: -1
                    backupRuleMaxAmount = (backupRule as? BackupRule.AmountLimit)?.backups ?: -1
                }
            }
        }

    suspend fun updateBackupTiming(timing: BackupTiming) = dataStore
        .updateData { prefs ->
            prefs.copy {
                backupPreferences = backupPreferences.copy {
                    backupTiming = when (timing) {
                        BackupTiming.Monthly -> BackupTimingProto.BACKUP_TIMING_MONTHLY
                        BackupTiming.Weekly -> BackupTimingProto.BACKUP_TIMING_WEEKLY
                        BackupTiming.Daily -> BackupTimingProto.BACKUP_TIMING_DAILY
                        BackupTiming.Dynamic -> BackupTimingProto.BACKUP_TIMING_DYNAMIC
                    }
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
                },
                backupPreferences = it.backupPreferences.toModel()
            )
        }

}

private fun com.snow.diary.core.datastore.BackupPreferences.toModel(): BackupPreferences =
    BackupPreferences(
        backupEnabled = backupEnabled,
        backupDirectoryUri = if (backupDirUri.isBlank()) null
        else try {
            URI(backupDirUri)
        } catch (_: Exception) {
            null
        },
        backupRule = if (backupRuleDays > 0) BackupRule.TimeLimit(
            Period.ofDays(
                backupRuleDays
            )
        )
        else if (backupRuleMaxAmount > 0) BackupRule.AmountLimit(backupRuleMaxAmount)
        else if (backupRuleMegabytes > 0) BackupRule.StorageLimit(backupRuleMegabytes)
        else BackupRule.Infinite,
        backupTiming = when (backupTiming) {
            BackupTimingProto.BACKUP_TIMING_MONTHLY -> BackupTiming.Monthly
            BackupTimingProto.BACKUP_TIMING_DAILY -> BackupTiming.Daily
            BackupTimingProto.BACKUP_TIMING_DYNAMIC -> BackupTiming.Dynamic
            else -> BackupTiming.Weekly
        }
    )