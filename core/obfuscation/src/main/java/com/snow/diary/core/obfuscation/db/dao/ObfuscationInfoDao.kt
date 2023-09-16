package com.snow.diary.core.obfuscation.db.dao;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.snow.diary.core.obfuscation.db.model.DreamObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.LocationObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.PersonObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.RelationObfuscationInfo

@Dao
interface ObfuscationInfoDao {

    @Insert
    fun addDreamInfo(vararg infos: DreamObfuscationInfo)

    @Insert
    fun addPersonIndo(vararg infos: PersonObfuscationInfo)

    @Insert
    fun addLocationInfo(vararg infos: LocationObfuscationInfo)

    @Insert
    fun addRelationInfo(vararg infos: RelationObfuscationInfo)




    @Query("SELECT * FROM dream_obfuscation_info")
    fun getAllDreamInfos(): List<DreamObfuscationInfo>

    @Query("SELECT * FROM person_obfuscation_info")
    fun getAllPersonInfos(): List<PersonObfuscationInfo>

    @Query("SELECT * FROM location_obfuscation_info")
    fun getAllLocationInfos(): List<LocationObfuscationInfo>

    @Query("SELECT * FROM relation_obfuscation_info")
    fun getAllRelationInfos(): List<RelationObfuscationInfo>




    @Query("DELETE FROM dream_obfuscation_info")
    fun deleteAllDreamInfos()

    @Query("DELETE FROM person_obfuscation_info")
    fun deleteAllPersonInfos()

    @Query("DELETE FROM location_obfuscation_info")
    fun deleteAllLocationInfos()

    @Query("DELETE FROM relation_obfuscation_info")
    fun deleteAllRelationInfos()



    @Query("DELETE FROM dream_obfuscation_info WHERE dreamId = :id")
    fun deleteDreamInfoById(id: Long)

    @Query("DELETE FROM person_obfuscation_info WHERE personId = :id")
    fun deletePersonInfoById(id: Long)

    @Query("DELETE FROM location_obfuscation_info WHERE locationId = :id")
    fun deleteLocationInfoById(id: Long)

    @Query("DELETE FROM relation_obfuscation_info WHERE relationId = :id")
    fun deleteRelationInfoById(id: Long)

}