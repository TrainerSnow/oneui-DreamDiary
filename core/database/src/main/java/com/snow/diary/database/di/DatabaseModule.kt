package com.snow.diary.database.di

import android.content.Context
import androidx.room.Room
import com.snow.diary.database.base.DiaryDatabase
import com.snow.diary.database.dao.DreamDao
import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.dao.RelationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
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
    fun dreamDao(
        db: DiaryDatabase
    ): DreamDao = db.dreamDao()

    @Provides
    fun personDao(
        db: DiaryDatabase
    ): PersonDao = db.personDao()

    @Provides
    fun locationDao(
        db: DiaryDatabase
    ): LocationDao = db.locationDao()

    @Provides
    fun relationDao(
        db: DiaryDatabase
    ): RelationDao = db.relationDao()

}