package com.snow.diary.relations.screen.add

import androidx.compose.ui.graphics.Color
import com.snow.diary.TextInput


data class AddRelationInputState (

    val name: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

    val color: Color = Color.Transparent

)

data class AddRelationUiState(

    val showColorPickerPopup: Boolean = false

)