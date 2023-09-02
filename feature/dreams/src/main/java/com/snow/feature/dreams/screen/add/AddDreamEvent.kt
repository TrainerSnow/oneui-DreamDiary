package com.snow.feature.dreams.screen.add;

import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person

sealed class AddDreamEvent {

    data class ChangeDescription(
        val description: String
    ) : AddDreamEvent()

    data class ChangeNote(
        val note: String
    ) : AddDreamEvent()

    data class ChangeMarkAsFavourite(
        val markAsFavourite: Boolean
    ) : AddDreamEvent()

    data class ChangeHappiness(
        val happiness: Float
    ) : AddDreamEvent()

    data class ChangeClearness(
        val clearness: Float
    ) : AddDreamEvent()

    data class ChangePersonQuery(
        val query: String
    ) : AddDreamEvent()

    data class ChangeLocationQuery(
        val query: String
    ) : AddDreamEvent()

    data class SelectPerson(
        val person: Person
    ): AddDreamEvent()

    data class SelectLocation(
        val location: Location
    ): AddDreamEvent()

    data class RemovePerson(
        val person: PersonWithRelation
    ): AddDreamEvent()

    data class RemoveLocation(
        val location: Location
    ): AddDreamEvent()

    data object ToggleAdvancedSettings: AddDreamEvent()

    data object TogglePersonPopup: AddDreamEvent()

    data object ToggleLocationPopup: AddDreamEvent()

}