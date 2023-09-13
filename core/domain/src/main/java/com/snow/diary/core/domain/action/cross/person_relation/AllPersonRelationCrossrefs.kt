package com.snow.diary.core.domain.action.cross.person_relation;

import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.FlowAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllPersonRelationCrossrefs(
    val crossrefDao: CrossrefDao
): FlowAction<Unit, List<Pair<Long, Long>>>() {
    override fun Unit.createFlow(): Flow<List<Pair<Long, Long>>> = crossrefDao
        .getAllPersonRelationCrossrefs()
        .map { crossrefs ->
            crossrefs.map {
                Pair(it.personId, it.relationId)
            }
        }

}