package com.snow.feature.dreams.screen.detail.component;

import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Person
import com.snow.feature.dreams.screen.detail.DreamDetailTabState

internal sealed class DreamDetailEvent {

    data class PersonFavouriteClick(
        val person: Person
    ): DreamDetailEvent()

    data class DeleteDream(
        val dream: Dream
    ): DreamDetailEvent()

    data class ChangeTabState(
        val tabState: DreamDetailTabState
    ): DreamDetailEvent()

}