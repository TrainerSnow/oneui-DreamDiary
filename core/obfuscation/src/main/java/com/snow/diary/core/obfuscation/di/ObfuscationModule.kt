package com.snow.diary.core.obfuscation.di

import android.content.Context
import androidx.room.Room
import com.snow.diary.core.obfuscation.db.ObfuscationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ObfuscationModule {

    @Provides
    @Singleton
    fun provideObfuscationDatabase(
        @ApplicationContext context: Context
    ) = Room
        .databaseBuilder(
            context = context,
            klass = ObfuscationDatabase::class.java,
            name = ObfuscationDatabase.DB_NAME
        )
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideObfuscationDao(
        database: ObfuscationDatabase
    ) = database.obfuscationDao

}