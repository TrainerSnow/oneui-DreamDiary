package com.snow.diary.core.domain.action.dream

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.pure.filterRange
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.domain.pure.sortWith
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DreamsFromPerson(
    val personDao: PersonDao
) : FlowAction<DreamsFromPerson.Input, List<Dream>>() {

    data class Input(

        val person: Person,

        val sortConfig: SortConfig = SortConfig(),

        val dateRange: DateRange = DateRange.AllTime

    )

    override fun Input.createFlow(): Flow<List<Dream>> = personDao
        .getPersonWithDreamsById(person.id!!)
        .map {
            it
                ?.dreams
                ?.mapToModel()
                ?.filterRange(dateRange)
                ?.sortWith(sortConfig)
                ?: emptyList()
        }

}