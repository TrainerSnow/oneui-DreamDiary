package com.snow.diary.domain.action.location;

import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.model.LocationEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Location

class DeleteLocation(
    private val locationDao: LocationDao
) : Action<Location, Unit>() {
    override suspend fun Location.compose() =
        locationDao
            .delete(LocationEntity(this))
}