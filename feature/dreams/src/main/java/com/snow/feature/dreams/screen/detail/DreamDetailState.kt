package com.snow.feature.dreams.screen.detail

import com.snow.diary.model.combine.DreamAggregate

internal sealed class DreamDetailState {

    data object Loading: DreamDetailState()

    data class Error(
        val id: Long,
        val msg: String? = null
    ): DreamDetailState()

    data class Success(
        val dream: DreamAggregate
    ): DreamDetailState()

}