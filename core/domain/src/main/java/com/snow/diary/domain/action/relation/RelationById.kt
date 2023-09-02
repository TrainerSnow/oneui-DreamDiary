package com.snow.diary.domain.action.relation;

import com.snow.diary.database.dao.RelationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.model.data.Relation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RelationById(
    val relationDao: RelationDao
): FlowAction<Long, Relation?>() {
    override fun Long.createFlow(): Flow<Relation?> = relationDao
        .getById(this)
        .map { it?.toModel() }

}