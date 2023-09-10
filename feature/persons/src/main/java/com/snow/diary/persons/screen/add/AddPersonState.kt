package com.snow.diary.persons.screen.add

import com.snow.diary.TextInput


data class AddPersonState (

    val name: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

    val markAsFavourite: Boolean = false,

    val relationQuery: String = ""

)