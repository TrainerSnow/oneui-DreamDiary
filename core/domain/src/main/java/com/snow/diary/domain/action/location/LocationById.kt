package com.snow.diary.domain.action.location;

import com.snow.diary.database.dao.LocationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.model.data.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationById(
    private val locationDao: LocationDao
): FlowAction<Long, Location?>() {
    override fun Long.createFlow(): Flow<Location?> = locationDao
        .getById(this)
        .map {
            it?.toModel()
        }

}