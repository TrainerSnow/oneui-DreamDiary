package com.snow.diary.database.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.IModelMappable
import com.snow.diary.model.data.Relation

@Entity(
    tableName = "person_relation"
)
data class RelationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val name: String,

    val color: Color,

    val notes: String?

): IModelMappable<Relation> {

    override fun toModel() = Relation(id, name, color.toArgb(), notes)

    constructor(relation: Relation) : this(
        relation.id,
        relation.name,
        Color.valueOf(relation.color),
        relation.notes
    )

}