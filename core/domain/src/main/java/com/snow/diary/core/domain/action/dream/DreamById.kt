package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.model.data.Dream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamById(
    val dreamDao: DreamDao
) : com.snow.diary.core.domain.action.FlowAction<Long, Dream?>() {
    override fun Long.createFlow(): Flow<Dream?> = dreamDao
        .getById(this)
        .map { it?.toModel() }

}