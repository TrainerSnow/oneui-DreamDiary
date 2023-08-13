package com.snow.diary.model.combine

import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation

data class PersonWithRelation(

    val person: Person,

    val relation: Relation

)
