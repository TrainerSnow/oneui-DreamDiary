package com.snow.diary.model.combine

import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location

data class DreamAggregate(

    val dream: Dream,

    val persons: List<PersonWithRelation>,

    val locations: List<Location>

)
