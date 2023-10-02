package com.snow.diary.core.domain.action.person

import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.data.Person

class AddPerson(
    private val personDao: PersonDao
): Action<Person, Long>() {
    override suspend fun Person.compose() = personDao
        .insert(PersonEntity(this))
        .first()

}