package com.snow.diary.domain.action.person;

import com.snow.diary.database.dao.PersonDao
import com.snow.diary.domain.action.FlowAction
import com.snow.diary.domain.pure.mapToModel
import com.snow.diary.domain.pure.sortWith
import com.snow.diary.model.data.Person
import com.snow.diary.model.sort.SortConfig

class AllPersons(
    val personDao: PersonDao
): FlowAction<AllPersons.Input, List<Person>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig(),
        //TODO: Add date range
    )

    override fun Input.createFlow() = personDao
        .getAll()
        .mapToModel()
        .sortWith(sortConfig)

}