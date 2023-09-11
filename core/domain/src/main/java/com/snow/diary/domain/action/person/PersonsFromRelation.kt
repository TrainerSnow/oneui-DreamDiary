package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.RelationDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonsFromRelation(
    val relationDao: RelationDao
) : FlowAction<Relation, List<Person>?>() {

    override fun Relation.createFlow(): Flow<List<Person>?> = relationDao
        .getRelationWithPersonsById(id!!)
        .map {
            it.persons
        }
        .mapToModel()

}