package com.snow.diary.core.domain.action.dream

import com.snow.diary.core.common.mapScoped
import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.filterRange
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.domain.pure.sortWith
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

@Suppress("MemberVisibilityCanBePrivate")
class AllDreams(
    val dreamDao: DreamDao
) : FlowAction<AllDreams.Input, List<Dream>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig(),
        val dateRange: DateRange = DateRange.AllTime
    )

    override fun Input.createFlow(): Flow<List<Dream>> =
        dreamDao
            .getAll()
            .mapScoped {
                mapToModel()
            }
            .mapScoped {
                sortWith(sortConfig)
                filterRange(dateRange)
            }


}