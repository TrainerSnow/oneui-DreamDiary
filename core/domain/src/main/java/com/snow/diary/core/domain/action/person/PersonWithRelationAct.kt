package com.snow.diary.core.domain.action.person;

import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.relation.RelationById
import com.snow.diary.core.model.combine.PersonWithRelation
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonWithRelationAct(
    val relationById: RelationById
) : com.snow.diary.core.domain.action.FlowAction<Person, PersonWithRelation>() {
    override fun Person.createFlow(): Flow<PersonWithRelation> =
        relationById(relationId)
            .map { it!! } //We know it exists, because we get a Person with the id passed
            .map {
                PersonWithRelation(this, it)
            }

}