package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.data.Dream

class DeleteDream(
    val dreamDao: DreamDao
): com.snow.diary.core.domain.action.Action<Dream, Unit>() {
    override suspend fun Dream.compose() = dreamDao
        .delete(
            DreamEntity(this)
        )

}