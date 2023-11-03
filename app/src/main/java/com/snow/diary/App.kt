package com.snow.diary

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.snow.diary.core.backup.worker.BackupWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var workerFactory: BackupWorkerFactory

    override fun onCreate() {
        super.onCreate()

        initWorkManagers()
    }

    private fun initWorkManagers() {
        val myConfig = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

        WorkManager.initialize(this, myConfig)
    }

}