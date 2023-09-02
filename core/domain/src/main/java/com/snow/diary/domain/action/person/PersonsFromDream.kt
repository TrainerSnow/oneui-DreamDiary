package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.DreamDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonsFromDream(
    val dreamDao: DreamDao
): FlowAction<Dream, List<Person>>() {
    override fun Dream.createFlow(): Flow<List<Person>> = dreamDao
        .getDreamWithPersonsById(id)
        .map { it!!.persons }
        .mapToModel()

}