package com.snow.diary.locations.screen.add

import com.snow.diary.TextInput


data class AddLocationState (

    val name: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

)