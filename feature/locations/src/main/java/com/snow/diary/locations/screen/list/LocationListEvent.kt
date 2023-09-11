package com.snow.diary.locations.screen.list

import com.snow.diary.model.sort.SortConfig

sealed class LocationListEvent {

    data class SortChange(
        val sortConfig: SortConfig
    ) : LocationListEvent()

}
