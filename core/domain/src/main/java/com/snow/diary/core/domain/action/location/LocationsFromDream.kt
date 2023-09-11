package com.snow.diary.core.domain.action.location;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationsFromDream(
    val dreamDao: DreamDao
): com.snow.diary.core.domain.action.FlowAction<Dream, List<Location>>() {
    override fun Dream.createFlow(): Flow<List<Location>> = dreamDao
        .getDreamWithLocationsById(id!!)
        .map { it?.locations ?: emptyList() }
        .mapToModel()

}