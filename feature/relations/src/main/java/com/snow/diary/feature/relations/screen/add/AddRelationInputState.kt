package com.snow.diary.feature.relations.screen.add

import androidx.compose.ui.graphics.Color
import com.snow.diary.core.form.TextInput


data class AddRelationInputState (

    val name: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

    val color: Color = Color.Transparent

)

data class AddRelationUiState(

    val showColorPickerPopup: Boolean = false

)