package com.snow.diary.domain.action.dream;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.domain.action.Action
import com.snow.diary.domain.pure.mapToEntity
import com.snow.diary.model.data.Dream

class UpdateDream(
    val dreamDao: DreamDao
) : Action<List<Dream>, Unit>() {
    override suspend fun List<Dream>.compose() = dreamDao
        .update(
            *mapToEntity()
                .toTypedArray()
        )

}