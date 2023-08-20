package com.snow.diary.data.repository

import com.snow.diary.model.data.Location
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun insert(vararg location: Location): List<Long>

    suspend fun update(vararg location: Location)

    suspend fun deleteLocation(vararg location: Location)

    fun getAllLocations(
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<Location>>

    fun getLocationById(id: Long): Flow<Location?>

}