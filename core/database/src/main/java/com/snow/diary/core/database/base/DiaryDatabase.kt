package com.snow.diary.core.database.base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snow.diary.core.database.cross.DreamLocationCrossref
import com.snow.diary.core.database.cross.DreamPersonCrossref
import com.snow.diary.core.database.cross.PersonRelationCrossref
import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.database.model.LocationEntity
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.database.model.RelationEntity
import com.snow.diary.core.database.type.ColorConverter
import com.snow.diary.core.database.type.DateConverters
import com.snow.diary.core.database.type.PointFConverters

@Database(
    entities = [
        DreamEntity::class,
        LocationEntity::class,
        PersonEntity::class,
        RelationEntity::class,
        DreamPersonCrossref::class,
        DreamLocationCrossref::class,
        PersonRelationCrossref::class
    ],
    version = DiaryDatabase.DB_VERSION
)
@TypeConverters(DateConverters::class, PointFConverters::class, ColorConverter::class)
abstract class DiaryDatabase : RoomDatabase() {


    abstract fun dreamDao(): DreamDao

    abstract fun personDao(): PersonDao

    abstract fun locationDao(): LocationDao

    abstract fun relationDao(): RelationDao

    abstract fun crossrefRao(): CrossrefDao

    companion object {

        const val DB_VERSION = 5

        const val DB_NAME = "Diary_Database"

    }

}