package com.snow.diary.core.domain.action.relation;

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.database.model.RelationEntity
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao

class DeleteRelation(
    val relationDao: RelationDao,
    private val obfuscationDao: ObfuscationInfoDao
) : com.snow.diary.core.domain.action.Action<Relation, Unit>() {

    override suspend fun Relation.compose() {
        relationDao
            .delete(RelationEntity(this))
        obfuscationDao
            .deleteRelationInfoById(id!!)
    }

}