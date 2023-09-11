package com.snow.diary.feature.relations.screen.list

import com.snow.diary.core.model.sort.SortConfig

sealed class RelationsListEvent {

    data class SortChange(
        val sortConfig: SortConfig
    ) : RelationsListEvent()

}
