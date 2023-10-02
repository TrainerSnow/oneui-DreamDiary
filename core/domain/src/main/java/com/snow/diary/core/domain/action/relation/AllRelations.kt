package com.snow.diary.core.domain.action.relation

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.domain.pure.sortWith
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

class AllRelations(
    val relationDao: RelationDao
): com.snow.diary.core.domain.action.FlowAction<AllRelations.Input, List<Relation>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig()
    )

    override fun Input.createFlow(): Flow<List<Relation>> = relationDao
        .getAll()
        .mapToModel()
        .sortWith(sortConfig)

}