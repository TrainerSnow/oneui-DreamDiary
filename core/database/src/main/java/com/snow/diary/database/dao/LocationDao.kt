package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.database.model.cross.LocationWithDreams
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationDao {
    @Transaction
    @Insert
    fun insert(vararg location: LocationEntity): List<Long>

    @Transaction
    @Update
    fun update(vararg location: LocationEntity)

    @Transaction
    @Delete
    fun delete(vararg location: LocationEntity)

    @Transaction
    @Query("SELECT * FROM Location")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Transaction
    @Query("SELECT * FROM Location WHERE locationId = :locationId")
    fun getLocationById(locationId: Long): Flow<LocationEntity?>

    @Transaction
    @Query("SELECT * FROM location WHERE locationId = :id")
    fun getLocationWithDreamsById(id: Long): Flow<LocationWithDreams?>

    @Transaction
    @Query("SELECT * FROM location")
    fun getAllLocationsWithDreams(): Flow<List<LocationWithDreams>>
}
