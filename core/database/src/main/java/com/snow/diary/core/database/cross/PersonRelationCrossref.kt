package com.snow.diary.core.database.cross

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.database.model.RelationEntity

@Entity(
    tableName = "person_relation_crossref",
    primaryKeys = ["personId", "relationId"],
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["personId"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RelationEntity::class,
            parentColumns = ["relationId"],
            childColumns = ["relationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PersonRelationCrossref(

    @ColumnInfo(index = true)
    val personId: Long,

    @ColumnInfo(index = true)
    val relationId: Long

)
