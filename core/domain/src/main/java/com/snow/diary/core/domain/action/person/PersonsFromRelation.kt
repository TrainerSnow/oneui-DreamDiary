package com.snow.diary.core.domain.action.person

import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
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