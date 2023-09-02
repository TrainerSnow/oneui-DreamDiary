package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.RelationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonWithRelationAct(
    val relationDao: RelationDao
) : FlowAction<Person, PersonWithRelation>() {
    override fun Person.createFlow(): Flow<PersonWithRelation> = relationDao
        .getById(id)
        .map { it!! }
        .mapToModel()
        .map {
            PersonWithRelation(this, it)
        }

}