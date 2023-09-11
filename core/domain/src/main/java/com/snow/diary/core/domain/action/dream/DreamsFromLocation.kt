package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamsFromLocation(
    val locationDao: LocationDao
): com.snow.diary.core.domain.action.FlowAction<Location, List<Dream>>() {
    override fun Location.createFlow(): Flow<List<Dream>> = locationDao
        .getLocationWithDreamsById(id!!)
        .map {
            it?.dreams?.mapToModel() ?: emptyList()
        }

}