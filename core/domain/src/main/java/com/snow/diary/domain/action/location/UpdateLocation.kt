package com.snow.diary.domain.action.location

import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Location

class UpdateLocation(
    private val locationDao: LocationDao
): Action<Location, Unit>() {
    override suspend fun Location.compose() = locationDao
        .update(LocationEntity(this))
}