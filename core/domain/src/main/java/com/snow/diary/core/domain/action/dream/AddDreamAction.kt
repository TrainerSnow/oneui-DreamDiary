package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.domain.pure.mapToEntity
import com.snow.diary.core.model.data.Dream

@Suppress("MemberVisibilityCanBePrivate")
class AddDreamAction(
    val dreamDao: DreamDao
) : com.snow.diary.core.domain.action.Action<List<Dream>, List<Long>>() {

    override suspend fun List<Dream>.compose(): List<Long> = dreamDao
        .insert(
            *mapToEntity()
                .toTypedArray()
        )

}