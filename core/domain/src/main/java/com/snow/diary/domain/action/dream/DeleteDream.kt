package com.snow.diary.domain.action.dream;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Dream

class DeleteDream(
    val dreamDao: DreamDao
): Action<Dream, Unit>() {
    override suspend fun Dream.compose() = dreamDao
        .delete(
            DreamEntity(this)
        )

}