package com.snow.diary.core.domain.action.relation

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.combine.RelationWithPersons
import com.snow.diary.core.model.data.Relation

class RelationsWithPersonsAct(
    val relationDao: RelationDao
) : FlowAction<Relation, List<RelationWithPersons>>() {

    override fun Relation.createFlow() = relationDao
        .getAllRelationsWithPersons()
        .mapToModel()

}