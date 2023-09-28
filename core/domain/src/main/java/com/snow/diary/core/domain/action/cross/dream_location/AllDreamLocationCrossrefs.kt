package com.snow.diary.core.domain.action.cross.dream_location;

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.model.data.Dream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class AllDreamLocationCrossrefs(
    private val crossrefDao: CrossrefDao,
    private val allDreams: AllDreams
) : FlowAction<AllDreamLocationCrossrefs.Input, List<Pair<Long, Long>>>() {

    data class Input(
        val dateRange: DateRange = DateRange.AllTime
    )

    override fun Input.createFlow(): Flow<List<Pair<Long, Long>>> = combine(
        flow = crossrefDao
            .getAllDreamLocationCrossrefs()
            .map { crossrefs ->
                crossrefs.map {
                    Pair(it.dreamId, it.locationId)
                }
            },
        flow2 = allDreams(AllDreams.Input(dateRange = this.dateRange))
    ) { crossrefs, dreams ->
        dreams.map(Dream::id).let { ids ->
            crossrefs.filter { it.first in ids }
        }
    }

}