package com.snow.diary.core.domain.action.relation;

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.model.data.Relation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RelationById(
    val relationDao: RelationDao
): com.snow.diary.core.domain.action.FlowAction<Long, Relation?>() {
    override fun Long.createFlow(): Flow<Relation?> = relationDao
        .getById(this)
        .map { it?.toModel() }

}