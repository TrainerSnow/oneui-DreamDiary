package com.snow.diary.core.domain.action.relation;

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.database.model.RelationEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.data.Relation

class AddRelation(
    private val relationDao: RelationDao
): com.snow.diary.core.domain.action.Action<Relation, Long>() {
    override suspend fun Relation.compose() = relationDao
        .insert(RelationEntity(this))
        .first()

}