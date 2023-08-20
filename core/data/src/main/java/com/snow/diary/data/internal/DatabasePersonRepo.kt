package com.snow.diary.data.internal;

import com.snow.diary.common.map
import com.snow.diary.common.sortedDirectional
import com.snow.diary.data.repository.PersonRepository
import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.model.PersonEntity
import com.snow.diary.database.model.asModel
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabasePersonRepo @Inject constructor(
    val personDao: PersonDao
) : PersonRepository {
    override suspend fun insert(vararg person: Person): List<Long> = personDao
        .insert(*person.map { PersonEntity(it) })

    override suspend fun update(vararg person: Person) = personDao
        .update(*person.map { PersonEntity(it) })

    override suspend fun deletePerson(vararg person: Person) = personDao
        .delete(*person.map { PersonEntity(it) })

    override fun getAllPersons(sortConfig: SortConfig): Flow<List<Person>> = personDao
        .getAllPersons()
        .map { persons ->
            persons
                .sort(sortConfig)
                .map(PersonEntity::asModel)
        }

    override fun getPersonById(id: Long): Flow<Person?> = personDao
        .getPersonById(id)
        .map { it?.asModel }

    override fun getPersonsByRelation(
        relation: Relation,
        sortConfig: SortConfig
    ): Flow<List<Person>> = personDao
        .getAllPersons()
        .map { persons ->
            persons
                .filter { person ->
                    person.relationId == relation.id
                }
                .sort(sortConfig)
                .map(PersonEntity::asModel)
        }
}

private fun List<PersonEntity>.sort(sortConfig: SortConfig): List<PersonEntity> =
    when (sortConfig.mode) {
        SortMode.Alphabetically -> sortedBy { it.name }
        SortMode.Relation -> sortedBy { it.relationId }
        SortMode.Length -> sortedBy { it.name.length }
        else -> this
    }.sortedDirectional(sortConfig.direction)