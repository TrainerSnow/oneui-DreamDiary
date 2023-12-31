package com.snow.diary.core.domain.action.person

import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonsWithRelationsAct(
    val personDao: PersonDao
) : FlowAction<Person, List<PersonWithRelations>>() {
    override fun Person.createFlow(): Flow<List<PersonWithRelations>> = personDao
        .getAllPersonsWithRelationsById()
        .map {
            it.map {
                it.toModel()
            }
        }
}