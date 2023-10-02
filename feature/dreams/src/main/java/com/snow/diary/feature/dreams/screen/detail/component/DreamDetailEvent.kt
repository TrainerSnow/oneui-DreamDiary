package com.snow.diary.feature.dreams.screen.detail.component

import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.feature.dreams.screen.detail.DreamDetailTabState

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