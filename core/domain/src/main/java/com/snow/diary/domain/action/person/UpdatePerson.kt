package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.model.PersonEntity
import com.snow.diary.domain.action.Action
import com.snow.diary.model.data.Person

class UpdatePerson(
    val personDao: PersonDao
): Action<Person, Unit>() {
    override suspend fun Person.compose() = personDao
        .update(PersonEntity(this))

}