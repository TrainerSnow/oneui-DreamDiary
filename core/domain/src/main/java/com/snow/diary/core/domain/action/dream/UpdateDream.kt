package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.domain.pure.mapToEntity
import com.snow.diary.core.model.data.Dream

class UpdateDream(
    val dreamDao: DreamDao
) : com.snow.diary.core.domain.action.Action<List<Dream>, Unit>() {
    override suspend fun List<Dream>.compose() = dreamDao
        .update(
            *mapToEntity()
                .toTypedArray()
        )

}