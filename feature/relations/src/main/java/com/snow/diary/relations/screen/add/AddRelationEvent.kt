package com.snow.diary.relations.screen.add;

import androidx.compose.ui.graphics.Color

internal sealed class AddRelationEvent {

    data class ChangeName(
        val name: String
    ): AddRelationEvent()

    data class ChangeNote(
        val note: String
    ): AddRelationEvent()

    data class ChangeColor(
        val color: Color
    ): AddRelationEvent()

    data object Save: AddRelationEvent()

    data object ToggleColorPopupVisibility: AddRelationEvent()

}