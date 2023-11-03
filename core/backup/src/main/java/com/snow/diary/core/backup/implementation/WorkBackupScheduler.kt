package com.snow.diary.core.backup.implementation;

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.snow.diary.core.backup.BackupScheduler
import com.snow.diary.core.backup.worker.BackupWorker
import com.snow.diary.core.model.preferences.BackupTiming
import java.time.Duration

private const val TAG = "com.snow.diary.worker.BackupWorker"

internal class WorkBackupScheduler(
    private val context: Context
) : BackupScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun schedule(backupTiming: BackupTiming) {
        if (backupTiming == BackupTiming.Dynamic) return

        val request = PeriodicWorkRequestBuilder<BackupWorker>(getDuration(backupTiming))
            .addTag(TAG)
            .setInitialDelay(Duration.ofSeconds(10))
            .build()
        workManager.cancelAllWorkByTag(TAG)
        workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, request)
    }

    override suspend fun cancelAll() {
        workManager.cancelAllWorkByTag(TAG)
    }

    private fun getDuration(timing: BackupTiming): Duration = when (timing) {
        BackupTiming.Monthly -> Duration.ofDays(30)
        BackupTiming.Weekly -> Duration.ofDays(7)
        else -> Duration.ofDays(1)
    }

}