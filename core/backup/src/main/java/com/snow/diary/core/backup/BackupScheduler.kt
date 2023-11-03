package com.snow.diary.core.backup

import com.snow.diary.core.model.preferences.BackupTiming

interface BackupScheduler {

    suspend fun schedule(backupTiming: BackupTiming)

    suspend fun cancelAll()

}