package com.snow.feature.dreams.screen.list;

import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig

sealed class DreamListEvent {

    data object MenuClick: DreamListEvent()

    data class DreamFavouriteClick(
        val dream: Dream
    ): DreamListEvent()


    data class SortChange(
        val sortConfig: SortConfig
    ) : DreamListEvent()

}