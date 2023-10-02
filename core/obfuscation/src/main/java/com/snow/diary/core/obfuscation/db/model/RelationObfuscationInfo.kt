package com.snow.diary.core.obfuscation.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "relation_obfuscation_info"
)
class RelationObfuscationInfo (

    @PrimaryKey(false)
    val relationId: Long,

    val relationName: String,

    val relationNote: String? = null

)