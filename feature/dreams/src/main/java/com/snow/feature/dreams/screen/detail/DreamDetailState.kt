package com.snow.feature.dreams.screen.detail

import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location

internal sealed class DreamDetailState {

    data object Loading: DreamDetailState()

    data class Error(
        val id: Long,
        val msg: String? = null
    ): DreamDetailState()

    data class Success(
        val dream: Dream,
        val locations: List<Location>,
        val persons: List<PersonWithRelation>
    ): DreamDetailState()

}