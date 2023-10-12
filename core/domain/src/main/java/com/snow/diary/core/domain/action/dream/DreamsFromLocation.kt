package com.snow.diary.core.domain.action.dream

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.filterRange
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.domain.pure.sortWith
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamsFromLocation(
    val locationDao: LocationDao
): FlowAction<DreamsFromLocation.Input, List<Dream>>() {

    data class Input(

        val location: Location,

        val sortConfig: SortConfig = SortConfig(),

        val dateRange: DateRange = DateRange.AllTime

    )

    override fun Input.createFlow(): Flow<List<Dream>> = locationDao
        .getLocationWithDreamsById(location.id!!)
        .map {
            it
                ?.dreams
                ?.mapToModel()
                ?.filterRange(dateRange)
                ?.sortWith(sortConfig)
                ?: emptyList()
        }

}