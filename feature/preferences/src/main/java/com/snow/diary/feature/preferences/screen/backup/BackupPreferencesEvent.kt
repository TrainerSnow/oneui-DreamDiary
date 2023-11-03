package com.snow.diary.feature.preferences.screen.backup

import com.snow.diary.core.model.preferences.BackupTiming

internal sealed class BackupPreferencesEvent {

    data object ToggleBackup: BackupPreferencesEvent()

    data class ChangeBackupRule(
        val rule: UIBackupRule
    ): BackupPreferencesEvent()

    data class ChangeBackupValue(
        val value: Int
    ): BackupPreferencesEvent()

    data class ChangeBackupTiming(
        val timing: BackupTiming
    ): BackupPreferencesEvent()

}
