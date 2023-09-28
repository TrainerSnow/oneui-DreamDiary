package com.snow.diary.core.domain.action.statistics;

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.cross.dream_location.AllDreamLocationCrossrefs
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.location.LocationById
import com.snow.diary.core.model.data.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

class LocationsWithAmount(
    private val allLocations: AllLocations,
    private val allDreamLocationCrossrefs: AllDreamLocationCrossrefs,
    private val locationFromId: LocationById
) : FlowAction<DateRange, List<LocationsWithAmount.LocationWithAmount>>() {

    data class LocationWithAmount(

        val location: Location,

        val amount: Int = 0

    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun DateRange.createFlow(): Flow<List<LocationWithAmount>> = combine(
        flow = allLocations(
            AllLocations.Input()
        ),
        flow2 = allDreamLocationCrossrefs(AllDreamLocationCrossrefs.Input(this))
    ) { locations, crossrefs ->
        val idNums = mutableMapOf<Long, Int>()
        locations.map(Location::id).forEach { idNums[it!!] = 0 }

        crossrefs.forEach {
            if (it.second in idNums.keys) idNums[it.second] = idNums[it.second]!! + 1
            else idNums[it.second] = 1
        }

        idNums
    }.flatMapMerge { idNums ->
        val locations = idNums.map {
            locationFromId(it.key)
        }
        if (locations.isEmpty()) return@flatMapMerge flowOf(emptyList())

        combine(locations) {
            val filtered = it
                .filterNotNull()

            val amounts = mutableListOf<LocationWithAmount>()
            filtered.forEach {
                amounts.add(LocationWithAmount(it, idNums[it.id]!!))
            }
            amounts
        }
    }

}