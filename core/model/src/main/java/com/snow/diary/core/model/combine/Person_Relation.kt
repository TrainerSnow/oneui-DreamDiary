package com.snow.diary.core.model.combine

import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation

data class PersonWithRelations(

    val person: Person,

    val relation: List<Relation>

)

data class RelationWithPersons(

    val relation: Relation,

    val persons: List<Person>

)