package com.snow.diary.core.backup.worker;

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.snow.diary.core.backup.BackupRepository
import com.snow.diary.core.domain.action.preferences.GetPreferences

class BackupWorkerFactory(
    private val getPreferences: GetPreferences,
    private val backupRepository: BackupRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        val workerKlass = Class.forName(workerClassName).asSubclass(CoroutineWorker::class.java)
        val constructor =
            workerKlass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
        val instance = constructor.newInstance(appContext, workerParameters)

        when (instance) {
            is BackupWorker -> {
                instance.getPreferences = getPreferences
                instance.backupRepository = backupRepository
            }
        }

        return instance
    }

}