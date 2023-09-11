package com.snow.diary.feature.locations.screen.list

import com.snow.diary.core.model.sort.SortConfig

sealed class LocationListEvent {

    data class SortChange(
        val sortConfig: SortConfig
    ) : LocationListEvent()

}
