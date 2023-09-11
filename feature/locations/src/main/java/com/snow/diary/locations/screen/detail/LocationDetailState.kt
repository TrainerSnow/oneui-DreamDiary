package com.snow.diary.locations.screen.detail;

import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Relation

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