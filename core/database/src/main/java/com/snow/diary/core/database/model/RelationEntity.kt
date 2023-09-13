package com.snow.diary.core.database.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.core.database.IModelMappable
import com.snow.diary.core.model.data.Relation

@Entity(
    tableName = "person_relation"
)
data class RelationEntity(

    @PrimaryKey(autoGenerate = true)
    val relationId: Long? = null,

    val name: String,

    val color: Color,

    val notes: String?

): IModelMappable<Relation> {

    override fun toModel() = Relation(relationId, name, color.toArgb(), notes)

    constructor(relation: Relation) : this(
        relation.id,
        relation.name,
        Color.valueOf(relation.color),
        relation.notes
    )

}