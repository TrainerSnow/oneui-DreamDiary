package com.snow.diary.core.model.preferences

data class ObfuscationPreferences (

    val obfuscationEnabled: Boolean,

    val obfuscateDreams: Boolean,

    val obfuscatePersons: Boolean,

    val obfuscateLocations: Boolean,

    val obfuscateRelations: Boolean

)