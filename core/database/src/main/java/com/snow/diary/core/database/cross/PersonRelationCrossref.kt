package com.snow.diary.core.database.cross

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "person_relation_crossref",
    primaryKeys = ["personId", "relationId"]
)
data class PersonRelationCrossref(

    @ColumnInfo(index = true)
    val personId: Long,

    @ColumnInfo(index = true)
    val relationId: Long

)
