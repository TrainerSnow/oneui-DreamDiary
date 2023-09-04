package com.snow.diary.domain.action.location;

import com.snow.diary.database.dao.LocationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.domain.pure.sortWith
import com.snow.diary.model.data.Location
import com.snow.diary.model.sort.SortConfig

class AllLocations(
    val locationDao: LocationDao
): FlowAction<AllLocations.Input, List<Location>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig(),
        //TODO: Add date range
    )

    override fun Input.createFlow() = locationDao
        .getAll()
        .mapToModel()
        .sortWith(sortConfig)

}