package com.snow.diary.io.data.entity

import com.snow.diary.model.data.Relation
import kotlinx.serialization.Serializable

@Serializable
data class RelationEntity(

    val id: Long? = null,

    val name: String,

    val color: Int

) {

    fun toModel() = Relation(id, name, color)

    constructor(relation: Relation) : this(
        relation.id, relation.name, relation.color
    )

}