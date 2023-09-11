package com.snow.diary.core.domain.action.person;

import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonFromId(
    private val personDao: PersonDao
): com.snow.diary.core.domain.action.FlowAction<Long, Person?>() {
    override fun Long.createFlow(): Flow<Person?> = personDao
        .getById(this)
        .map {
            it?.toModel()
        }

}