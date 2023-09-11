package com.snow.diary.core.domain.action.location

import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.model.LocationEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.data.Location

class UpdateLocation(
    private val locationDao: LocationDao
): com.snow.diary.core.domain.action.Action<Location, Unit>() {
    override suspend fun Location.compose() = locationDao
        .update(LocationEntity(this))
}