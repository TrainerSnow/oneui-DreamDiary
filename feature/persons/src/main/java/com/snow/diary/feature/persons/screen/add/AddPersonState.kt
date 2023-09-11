package com.snow.diary.feature.persons.screen.add

import com.snow.diary.core.form.TextInput


data class AddPersonState (

    val name: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

    val markAsFavourite: Boolean = false,

    val relationQuery: String = ""

)