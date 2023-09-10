package com.snow.diary.persons.screen.detail;

import com.snow.diary.model.data.Dream

internal sealed class PersonDetailEvent {

    data class DreamFavouriteClick(
        val dream: Dream
    ): PersonDetailEvent()

    data class ChangeTab(
        val tab: PersonDetailTab
    ): PersonDetailEvent()


    data object Delete: PersonDetailEvent()

}