package com.snow.diary.core.domain.action.person

import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.domain.pure.sortWith
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.sort.SortConfig

class AllPersons(
    val personDao: PersonDao
): com.snow.diary.core.domain.action.FlowAction<AllPersons.Input, List<Person>>() {

    data class Input(
        val sortConfig: SortConfig = SortConfig()
    )

    override fun Input.createFlow() = personDao
        .getAll()
        .mapToModel()
        .sortWith(sortConfig)

}