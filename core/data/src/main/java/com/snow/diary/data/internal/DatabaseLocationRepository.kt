package com.snow.diary.data.internal;

import com.snow.diary.common.map
import com.snow.diary.common.sortedDirectional
import com.snow.diary.data.repository.LocationRepository
import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.database.model.asModel
import com.snow.diary.model.data.Location
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseLocationRepository @Inject constructor(
    val locationDao: LocationDao
): LocationRepository {
    override suspend fun insert(vararg location: Location): List<Long> = locationDao
        .insert(*location.map { LocationEntity(it) })

    override suspend fun update(vararg location: Location) = locationDao
        .update(*location.map { LocationEntity(it) })

    override suspend fun deleteLocation(vararg location: Location) = locationDao
        .delete(*location.map { LocationEntity(it) })

    override fun getAllLocations(sortConfig: SortConfig): Flow<List<Location>> = locationDao
        .getAllLocations()
        .map { locations ->
            locations
                .sort(sortConfig)
                .map(LocationEntity::asModel)
        }

    override fun getLocationById(id: Long): Flow<Location?> = locationDao
        .getLocationById(id)
        .map { it?.asModel }
}

private fun List<LocationEntity>.sort(sortConfig: SortConfig): List<LocationEntity> =
    when (sortConfig.mode) {
        SortMode.Alphabetically -> sortedBy { it.name }
        SortMode.Length -> sortedBy { it.name.length }
        else -> this
    }.sortedDirectional(sortConfig.direction)