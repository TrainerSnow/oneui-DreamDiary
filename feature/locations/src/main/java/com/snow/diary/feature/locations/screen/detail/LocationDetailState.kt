package com.snow.diary.feature.locations.screen.detail;

import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Relation

internal sealed class LocationDetailState {

    data object Loading: LocationDetailState()

    data class Error(
        val msg: String? = null,
        val id: Long
    ): LocationDetailState()

    data class Success(
        val location: Location,
        val dreams: List<Dream>
    ): LocationDetailState()

}