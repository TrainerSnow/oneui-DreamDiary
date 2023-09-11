package com.snow.diary.domain.action.dream;

import com.snow.diary.database.dao.LocationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamsFromLocation(
    val locationDao: LocationDao
): FlowAction<Location, List<Dream>>() {
    override fun Location.createFlow(): Flow<List<Dream>> = locationDao
        .getLocationWithDreamsById(id!!)
        .map {
            it?.dreams?.mapToModel() ?: emptyList()
        }

}