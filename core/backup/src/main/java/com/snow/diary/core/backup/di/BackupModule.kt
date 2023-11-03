package com.snow.diary.core.backup.di

import android.content.Context
import com.snow.diary.core.backup.BackupRepository
import com.snow.diary.core.backup.implementation.FileBackupRepository
import com.snow.diary.core.domain.action.io.GetIOData
import com.snow.diary.core.domain.action.io.ImportIOData
import com.snow.diary.core.domain.action.preferences.GetPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupModule {

    @Provides
    @Singleton
    fun provideBackupRepository(
        @ApplicationContext context: Context,
        getPreferences: GetPreferences,
        getIOData: GetIOData,
        importIOData: ImportIOData
    ): BackupRepository = FileBackupRepository(getPreferences, getIOData, importIOData, context)

}