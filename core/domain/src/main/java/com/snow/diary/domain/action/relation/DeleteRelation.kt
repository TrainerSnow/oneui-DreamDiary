package com.snow.diary.domain.action.relation;

import com.snow.diary.database.dao.RelationDao
import com.snow.diary.database.model.RelationEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Relation

class DeleteRelation(
    val relationDao: RelationDao
) : Action<Relation, Unit>() {

    override suspend fun Relation.compose() = relationDao
        .delete(RelationEntity(this))

}