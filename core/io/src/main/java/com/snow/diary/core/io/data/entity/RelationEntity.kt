package com.snow.diary.core.io.data.entity

import com.snow.diary.core.model.data.Relation
import kotlinx.serialization.Serializable

@Serializable
data class RelationEntity(

    val id: Long? = null,

    val name: String,

    val color: Int,

    val notes: String?

) {

    fun toModel() = Relation(id, name, color, notes)

    constructor(relation: Relation) : this(
        relation.id, relation.name, relation.color, relation.notes
    )

}