package com.snow.diary.core.domain.action.person

import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao

class DeletePerson(
    private val personDao: PersonDao,
    private val obfuscationDao: ObfuscationInfoDao
) : com.snow.diary.core.domain.action.Action<Person, Unit>() {
    override suspend fun Person.compose() {
        personDao
            .delete(PersonEntity(this))
        obfuscationDao
            .deletePersonInfoById(id!!)
    }
}