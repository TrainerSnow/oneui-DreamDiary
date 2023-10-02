package com.snow.diary.core.domain.action.relation

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.model.combine.RelationWithPersons
import com.snow.diary.core.model.data.Relation
import kotlinx.coroutines.flow.map

class RelationWithPersonsAct(
    val relationDao: RelationDao
) : FlowAction<Relation, RelationWithPersons?>() {

    override fun Relation.createFlow() = relationDao
        .getRelationWithPersonsById(id!!)
        .map {
            it.toModel()
        }
}