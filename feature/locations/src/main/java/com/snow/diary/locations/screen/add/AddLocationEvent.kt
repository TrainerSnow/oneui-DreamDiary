package com.snow.diary.locations.screen.add;

internal sealed class AddLocationEvent {

    data class ChangeName(
        val name: String
    ): AddLocationEvent()

    data class ChangeNote(
        val note: String
    ): AddLocationEvent()

    data object Save: AddLocationEvent()

}