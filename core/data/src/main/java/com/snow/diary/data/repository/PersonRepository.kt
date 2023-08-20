package com.snow.diary.data.repository

import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    suspend fun insert(vararg person: Person): List<Long>


    suspend fun update(vararg person: Person)

    suspend fun deletePerson(vararg person: Person)

    fun getAllPersons(
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<Person>>

    fun getPersonById(id: Long): Flow<Person?>

    fun getPersonsByRelation(
        relation: Relation,
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<Person>>

}