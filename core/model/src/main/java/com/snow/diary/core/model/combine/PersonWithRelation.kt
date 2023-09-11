package com.snow.diary.core.model.combine

import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation

data class PersonWithRelation(

    val person: Person,

    val relation: Relation

)
