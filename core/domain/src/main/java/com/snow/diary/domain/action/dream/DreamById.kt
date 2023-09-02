package com.snow.diary.domain.action.dream;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.model.data.Dream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamById(
    val dreamDao: DreamDao
) : FlowAction<Long, Dream?>() {
    override fun Long.createFlow(): Flow<Dream?> = dreamDao
        .getById(this)
        .map { it?.toModel() }

}