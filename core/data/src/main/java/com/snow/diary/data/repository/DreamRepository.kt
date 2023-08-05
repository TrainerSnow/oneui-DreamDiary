package com.snow.diary.data.repository

import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

interface DreamRepository {

    suspend fun upsertDream(vararg dream: Dream)

    suspend fun deleteDream(vararg dream: Dream)

    fun getAllDreams(
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<Dream>>

    fun getDreamById(id: Long): Flow<Dream?>



}