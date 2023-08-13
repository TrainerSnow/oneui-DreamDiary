package com.snow.diary.database.model.cross

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.snow.diary.database.cross.DreamPersonCrossref
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.PersonEntity

data class DreamWithPersons(

    @Embedded val dream: DreamEntity,

    @Relation(
        parentColumn = "dreamId",
        entityColumn = "personId",
        associateBy = Junction(DreamPersonCrossref::class)
    )
    val persons: List<PersonEntity>

)

data class PersonWithDreams(

    @Embedded val person: PersonEntity,

    @Relation(
        parentColumn = "personId",
        entityColumn = "dreamId",
        associateBy = Junction(DreamPersonCrossref::class)
    )
    val dreams: List<DreamEntity>

)
