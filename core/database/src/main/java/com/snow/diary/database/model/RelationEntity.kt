package com.snow.diary.database.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "person_relation"
)
data class RelationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,

    val color: Color

)