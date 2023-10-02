package com.snow.diary.core.domain.action.person

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonsFromDream(
    val dreamDao: DreamDao
): FlowAction<Dream, List<Person>>() {
    override fun Dream.createFlow(): Flow<List<Person>> = dreamDao
        .getDreamWithPersonsById(id!!)
        .map { it?.persons ?: emptyList() }
        .mapToModel()

}