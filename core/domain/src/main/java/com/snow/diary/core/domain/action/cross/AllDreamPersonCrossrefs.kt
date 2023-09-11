package com.snow.diary.core.domain.action.cross;

import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.FlowAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllDreamPersonCrossrefs(
    val crossrefDao: CrossrefDao
): com.snow.diary.core.domain.action.FlowAction<Unit, List<Pair<Long, Long>>>() {
    override fun Unit.createFlow(): Flow<List<Pair<Long, Long>>> = crossrefDao
        .getAllDreamPersonCrossrefs()
        .map { crossrefs ->
            crossrefs.map {
                Pair(it.dreamId, it.personId)
            }
        }

}