package com.snow.diary.core.domain.action.cross.dream_location;

import com.snow.diary.core.database.dao.CrossrefDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllDreamLocationCrossrefs(
    val crossrefDao: CrossrefDao
): com.snow.diary.core.domain.action.FlowAction<Unit, List<Pair<Long, Long>>>() {
    override fun Unit.createFlow(): Flow<List<Pair<Long, Long>>> = crossrefDao
        .getAllDreamLocationCrossrefs()
        .map { crossrefs ->
            crossrefs.map {
                Pair(it.dreamId, it.locationId)
            }
        }

}