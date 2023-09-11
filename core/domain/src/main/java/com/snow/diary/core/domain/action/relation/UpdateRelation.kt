package com.snow.diary.core.domain.action.relation;

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.database.model.RelationEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.data.Relation

class UpdateRelation(
    private val relationDao: RelationDao
): com.snow.diary.core.domain.action.Action<Relation, Unit>() {
    override suspend fun Relation.compose() = relationDao
        .update(RelationEntity(this))

}