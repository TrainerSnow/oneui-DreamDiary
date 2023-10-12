package com.snow.diary.core.database.cross

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.database.model.PersonEntity

@Entity(
    tableName = "dream_person_crossref",
    primaryKeys = ["dreamId", "personId"],
    foreignKeys = [
        ForeignKey(
            entity = DreamEntity::class,
            parentColumns = ["dreamId"],
            childColumns = ["dreamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["personId"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DreamPersonCrossref(

    @ColumnInfo(index = true)
    val dreamId: Long,

    @ColumnInfo(index = true)
    val personId: Long

)
