package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.database.model.cross.LocationWithDreams
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationDao {
    @Transaction
    @Upsert
    fun upsert(vararg location: LocationEntity)

    @Transaction
    @Delete
    fun delete(vararg location: LocationEntity)

    @Transaction
    @Query("SELECT * FROM Location")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Transaction
    @Query("SELECT * FROM Location WHERE id = :locationId")
    fun getLocationById(locationId: Long): Flow<LocationEntity?>

    @Transaction
    @Query("SELECT * FROM location WHERE id = :id")
    fun getLocationWithDreamsById(id: Long): Flow<LocationWithDreams?>

    @Transaction
    @Query("SELECT * FROM location")
    fun getAllLocationsWithDreams(): Flow<List<LocationWithDreams>>
}
