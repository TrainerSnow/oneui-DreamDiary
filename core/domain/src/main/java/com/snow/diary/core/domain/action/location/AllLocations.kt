package com.snow.diary.core.domain.action.location;

import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.domain.pure.sortWith
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.sort.SortConfig

class AllLocations(
    val locationDao: LocationDao
): com.snow.diary.core.domain.action.FlowAction<AllLocations.Input, List<Location>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig(),
        //TODO: Add date range
    )

    override fun Input.createFlow() = locationDao
        .getAll()
        .mapToModel()
        .sortWith(sortConfig)

}