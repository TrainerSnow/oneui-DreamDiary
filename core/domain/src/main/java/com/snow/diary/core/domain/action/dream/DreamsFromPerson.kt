package com.snow.diary.core.domain.action.dream

import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamsFromPerson(
    val personDao: PersonDao
): FlowAction<Person, List<Dream>>() {
    override fun Person.createFlow(): Flow<List<Dream>> = personDao
        .getPersonWithDreamsById(id!!)
        .map {
            it?.dreams?.mapToModel() ?: emptyList()
        }

}