package com.snow.diary.feature.dreams.screen.detail

import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location

internal sealed class DreamDetailState {

    data object Loading: DreamDetailState()

    data class Error(
        val id: Long,
        val msg: String? = null
    ): DreamDetailState()

    data class Success(
        val dream: Dream,
        val locations: List<Location>,
        val persons: List<PersonWithRelations>
    ): DreamDetailState()

}