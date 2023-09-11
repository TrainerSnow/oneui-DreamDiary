package com.snow.diary.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class RelationWithPersons(

    @Embedded val relation: RelationEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "relationId"
    )
    val persons: List<PersonEntity>
)
