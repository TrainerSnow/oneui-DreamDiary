package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.model.PersonEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Person

class AddPerson(
    private val personDao: PersonDao
): Action<Person, Long>() {
    override suspend fun Person.compose() = personDao
        .insert(PersonEntity(this))
        .first()

}