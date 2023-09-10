package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.PersonDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonFromId(
    private val personDao: PersonDao
): FlowAction<Long, Person?>() {
    override fun Long.createFlow(): Flow<Person?> = personDao
        .getById(this)
        .map {
            it?.toModel()
        }

}