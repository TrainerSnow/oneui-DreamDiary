package com.snow.diary.data.internal;

import com.snow.diary.common.map
import com.snow.diary.common.sortedDirectional
import com.snow.diary.data.repository.DreamRepository
import com.snow.diary.database.dao.DreamDao
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.asModel
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseDreamRepo @Inject constructor(
    val dreamDao: DreamDao
) : DreamRepository {
    override suspend fun upsertDream(vararg dream: Dream) = dreamDao
        .upsert(*dream.map { DreamEntity(it) })

    override suspend fun deleteDream(vararg dream: Dream) = dreamDao
        .delete(*dream.map { DreamEntity(it) })

    override fun getAllDreams(sortConfig: SortConfig): Flow<List<Dream>> = dreamDao
        .getAllDreams()
        .map { dreams ->
            dreams
                .sort(sortConfig)
                .map(DreamEntity::asModel)
        }

    override fun getDreamById(id: Long): Flow<Dream?> = dreamDao
        .getDreamById(id)
        .map { it?.asModel }
}

private fun List<DreamEntity>.sort(sortConfig: SortConfig): List<DreamEntity> =
    when (sortConfig.mode) {
        SortMode.Created -> sortedBy { it.created }
        SortMode.Updated -> sortedBy { it.updated }
        SortMode.Alphabetically -> sortedBy { it.description }
        SortMode.Happiness -> sortedBy { it.happiness ?: 0F }
        SortMode.Clearness -> sortedBy { it.clearness ?: 0F }
        SortMode.Length -> sortedBy { it.description.length }
        else -> this
    }.sortedDirectional(sortConfig.direction)