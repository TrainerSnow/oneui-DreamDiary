package com.snow.diary.relations.screen.list

import com.snow.diary.model.sort.SortConfig

sealed class RelationsListEvent {

    data class SortChange(
        val sortConfig: SortConfig
    ) : RelationsListEvent()

}
