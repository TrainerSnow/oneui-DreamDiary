package com.snow.diary.core.backup.worker;

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.snow.diary.core.backup.BackupRepository
import com.snow.diary.core.backup.model.BackupCreationResult
import com.snow.diary.core.domain.action.preferences.GetPreferences
import kotlinx.coroutines.flow.first

internal class BackupWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    lateinit var getPreferences: GetPreferences
    lateinit var backupRepository: BackupRepository

    override suspend fun doWork(): Result {
        val result = backupRepository.createBackup()

        val rule = getPreferences(Unit)
            .first()
            .backupPreferences
            .backupRule
        backupRepository.ensureRule(rule)

        return when (result) {
            is BackupCreationResult.Success -> Result.success()
            else -> Result.failure(
                Data.Builder()
                    .putString("error", result.toString())
                    .build()
            )
        }
    }


}