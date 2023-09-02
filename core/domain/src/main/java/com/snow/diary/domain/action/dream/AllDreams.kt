package com.snow.diary.domain.action.dream;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.domain.pure.sortWith
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

@Suppress("MemberVisibilityCanBePrivate")
class AllDreams(
    val dreamDao: DreamDao
) : FlowAction<AllDreams.Input, List<Dream>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig(),
        //TODO: Add time range param
    )

    override fun Input.createFlow(): Flow<List<Dream>> =
        dreamDao
            .getAll()
            .mapToModel()
            .sortWith(sortConfig)


}