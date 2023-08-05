package com.snow.diary.database.base;

import androidx.room.Database
import androidx.room.RoomDatabase
import com.snow.diary.database.dao.DreamDao
import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.dao.RelationDao
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.database.model.PersonEntity
import com.snow.diary.database.model.RelationEntity

@Database(
    entities = [
        DreamEntity::class,
        LocationEntity::class,
        PersonEntity::class,
        RelationEntity::class
    ],
    version = DiaryDatabase.DB_VERSION
)
abstract class DiaryDatabase : RoomDatabase() {


    abstract fun dreamDao(): DreamDao

    abstract fun personDao(): PersonDao

    abstract fun locationDao(): LocationDao

    abstract fun relationDao(): RelationDao

    companion object {

        const val DB_VERSION = 1

        const val DB_NAME = "Diary_Database"

    }

}