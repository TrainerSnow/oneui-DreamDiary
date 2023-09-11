package com.snow.diary.relations.screen.detail;

import com.snow.diary.model.data.Person

internal sealed class RelationDetailEvent {

    data class PersonFavouriteClick(
        val person: Person
    ): RelationDetailEvent()

    data class ChangeTab(
        val tab: RelationDetailTab
    ): RelationDetailEvent()


    data object Delete: RelationDetailEvent()

}