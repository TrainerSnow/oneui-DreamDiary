package com.snow.diary.domain.action.dream;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.domain.action.Action
import com.snow.diary.domain.pure.mapToEntity
import com.snow.diary.model.data.Dream

@Suppress("MemberVisibilityCanBePrivate")
class AddDreamAction(
    val dreamDao: DreamDao
) : Action<List<Dream>, List<Long>>() {

    override suspend fun List<Dream>.compose(): List<Long> = dreamDao
        .insert(
            *mapToEntity()
                .toTypedArray()
        )

}