package com.snow.diary.feature.preferences.screen.backup

import android.net.Uri
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

    data class ChangeUri(
        val uri: Uri
    ): BackupPreferencesEvent()

}
