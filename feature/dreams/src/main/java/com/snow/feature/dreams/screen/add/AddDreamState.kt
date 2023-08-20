package com.snow.feature.dreams.screen.add;

import com.snow.diary.TextInput
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person

/**
 * Holds the raw user input
 */
internal data class AddDreamInputState (

    val description: TextInput = TextInput(""),

    val note: TextInput = TextInput(""),

    val markAsFavourite: Boolean = false,

    val happiness: Float? = null,

    val clearness: Float? = null,

    val personQuery: TextInput = TextInput(""),

    val locationQuery: TextInput = TextInput("")

)

/**
 * Holds the extras the user selected
 */
internal data class AddDreamExtrasState (

    val persons: List<Person> = emptyList(),

    val locations: List<Location> = emptyList()

)

/**
 * Holds the extras resolved by the users search queries
 */
internal data class AddDreamQueryState (

    val persons: List<Person> = emptyList(),

    val locations: List<Location> = emptyList()

)

/**
 * Holds information about the ui
 */
internal data class AddDreamUiState (

    val showPersonsPopup: Boolean = false,

    val showLocationsPopup: Boolean = false,

    val showAdvancedSettings: Boolean = false

)
