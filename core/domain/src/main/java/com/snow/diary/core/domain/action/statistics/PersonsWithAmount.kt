package com.snow.diary.core.domain.action.statistics;

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.person.PersonFromId
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge

class PersonsWithAmount(
    private val allDreams: AllDreams,
    private val crossrefDao: CrossrefDao,
    private val personFromId: PersonFromId
) : FlowAction<DateRange, List<PersonsWithAmount.PersonWithAmount>>() {

    data class PersonWithAmount(

        val person: Person,

        val amount: Int = 0

    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun DateRange.createFlow(): Flow<List<PersonWithAmount>> = combine(
        flow = allDreams(
            AllDreams.Input(dateRange = this)
        ),
        flow2 = crossrefDao.getAllDreamPersonCrossrefs()
    ) { dreams, crossrefs ->
        val idNums = mutableMapOf<Long, Int>()

        crossrefs.forEach {
            if (it.personId in idNums.keys) idNums[it.personId] = idNums[it.personId]!! + 1
            else idNums[it.personId] = 1
        }

        idNums
    }.flatMapMerge { idNums ->
        val persons = idNums.map {
            personFromId(it.key)
        }

        combine(persons) {
            val filtered = it
                .filterNotNull()

            val amounts = mutableListOf<PersonWithAmount>()
            filtered.forEach {
                amounts.add(PersonWithAmount(it, idNums[it.id]!!))
            }
            amounts
        }
    }

}