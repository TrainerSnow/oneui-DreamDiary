package com.snow.diary.feature.locations.screen.add

import com.snow.diary.core.form.TextInput


data class AddLocationState (

    val name: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

    )