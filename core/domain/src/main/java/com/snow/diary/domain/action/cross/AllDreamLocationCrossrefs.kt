package com.snow.diary.domain.action.cross;

import com.snow.diary.database.dao.CrossrefDao
import com.snow.diary.domain.action.FlowAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllDreamLocationCrossrefs(
    val crossrefDao: CrossrefDao
): FlowAction<Unit, List<Pair<Long, Long>>>() {
    override fun Unit.createFlow(): Flow<List<Pair<Long, Long>>> = crossrefDao
        .getAllDreamLocationCrossrefs()
        .map { crossrefs ->
            crossrefs.map {
                Pair(it.dreamId, it.locationId)
            }
        }

}