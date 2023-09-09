package com.snow.diary.domain.action.relation;

import com.snow.diary.database.dao.RelationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.domain.pure.sortWith
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

class AllRelations(
    val relationDao: RelationDao
): FlowAction<AllRelations.Input, List<Relation>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig(),
        //TODO: Add date range
    )

    override fun Input.createFlow(): Flow<List<Relation>> = relationDao
        .getAll()
        .mapToModel()
        .sortWith(sortConfig)

}