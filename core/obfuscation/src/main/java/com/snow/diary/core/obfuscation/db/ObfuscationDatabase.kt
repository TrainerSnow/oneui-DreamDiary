package com.snow.diary.core.obfuscation.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao
import com.snow.diary.core.obfuscation.db.model.DreamObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.LocationObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.PersonObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.RelationObfuscationInfo


@Database(
    entities = [
        DreamObfuscationInfo::class,
        PersonObfuscationInfo::class,
        LocationObfuscationInfo::class,
        RelationObfuscationInfo::class
    ],
    version = ObfuscationDatabase.DB_VERSION
)
abstract class ObfuscationDatabase : RoomDatabase() {

    abstract val obfuscationDao: ObfuscationInfoDao

    companion object {

        const val DB_NAME = "Obfuscation Database"

        const val DB_VERSION = 1

    }

}