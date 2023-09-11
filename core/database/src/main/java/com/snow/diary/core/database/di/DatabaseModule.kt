package com.snow.diary.core.database.di

import android.content.Context
import androidx.room.Room
import com.snow.diary.core.database.base.DiaryDatabase
import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context
    ): DiaryDatabase = Room.databaseBuilder(
        context = context,
        klass = DiaryDatabase::class.java,
        name = DiaryDatabase.DB_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun dreamDao(
        db: DiaryDatabase
    ): DreamDao = db.dreamDao()

    @Provides
    @Singleton
    fun personDao(
        db: DiaryDatabase
    ): PersonDao = db.personDao()

    @Provides
    @Singleton
    fun locationDao(
        db: DiaryDatabase
    ): LocationDao = db.locationDao()

    @Provides
    @Singleton
    fun relationDao(
        db: DiaryDatabase
    ): RelationDao = db.relationDao()

    @Provides
    @Singleton
    fun crossrefDao(
        db: DiaryDatabase
    ): CrossrefDao = db.crossrefRao()

}