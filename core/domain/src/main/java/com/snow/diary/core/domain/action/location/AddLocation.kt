package com.snow.diary.core.domain.action.location

import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.model.LocationEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.data.Location

class AddLocation(
    private val locationDao: LocationDao
): Action<Location, Long>() {
    override suspend fun Location.compose(): Long = locationDao
        .insert(LocationEntity(this))
        .first()

}