package com.snow.diary.feature.preferences.screen.backup

internal sealed class BackupPreferencesEvent {

    data object ToggleBackup: BackupPreferencesEvent()

    data class ChangeBackupRule(
        val rule: UIBackupRule
    ): BackupPreferencesEvent()

    data class ChangeBackupValue(
        val value: Int
    ): BackupPreferencesEvent()

}
