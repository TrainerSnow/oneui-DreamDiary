package com.snow.diary.domain.action.location;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationsFromDream(
    val dreamDao: DreamDao
): FlowAction<Dream, List<Location>>() {
    override fun Dream.createFlow(): Flow<List<Location>> = dreamDao
        .getDreamWithLocationsById(id!!)
        .map { it?.locations ?: emptyList() }
        .mapToModel()

}