package com.snow.diary.database.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.model.data.Relation

@Entity(
    tableName = "person_relation"
)
data class RelationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,

    val color: Color

) {

    constructor(relation: Relation) : this(
        relation.id,
        relation.name,
        relation.color
    )

}

val RelationEntity.asModel: Relation
    get() = Relation(id, name, color)