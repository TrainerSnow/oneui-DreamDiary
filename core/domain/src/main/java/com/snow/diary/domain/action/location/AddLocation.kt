package com.snow.diary.domain.action.location;

import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Location

class AddLocation(
    private val locationDao: LocationDao
): Action<Location, Long>() {
    override suspend fun Location.compose(): Long = locationDao
        .insert(LocationEntity(this))
        .first()

}