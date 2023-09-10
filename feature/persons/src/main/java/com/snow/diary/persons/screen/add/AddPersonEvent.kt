package com.snow.diary.persons.screen.add;

import com.snow.diary.model.data.Relation

internal sealed class AddPersonEvent {

    data class ChangeName(
        val name: String
    ): AddPersonEvent()

    data class ChangeNote(
        val note: String
    ): AddPersonEvent()

    data class ChangeMarkAsFavourite(
        val favourite: Boolean
    ): AddPersonEvent()

    data class UnselectRelation(
        val relation: Relation
    ): AddPersonEvent()

    data class SelectRelation(
        val relation: Relation
    ): AddPersonEvent()

    data class ChangeRelationQUery(
        val query: String
    ): AddPersonEvent()

    data object ToggleRelationsPopup: AddPersonEvent()

    data object Save: AddPersonEvent()

}