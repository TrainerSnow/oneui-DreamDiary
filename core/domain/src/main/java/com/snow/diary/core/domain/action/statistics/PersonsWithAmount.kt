package com.snow.diary.core.domain.action.statistics;

import com.snow.diary.core.common.time.DateRange
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.cross.dream_person.AllDreamPersonCrossrefs
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.person.PersonFromId
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

class PersonsWithAmount(
    private val allPersons: AllPersons,
    private val allDreamPersonCrossrefs: AllDreamPersonCrossrefs,
    private val personFromId: PersonFromId
) : FlowAction<DateRange, List<PersonsWithAmount.PersonWithAmount>>() {

    data class PersonWithAmount(

        val person: Person,

        val amount: Int = 0

    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun DateRange.createFlow(): Flow<List<PersonWithAmount>> = combine(
        flow = allPersons(
            AllPersons.Input()
        ),
        flow2 = allDreamPersonCrossrefs(AllDreamPersonCrossrefs.Input(this))
    ) { persons, crossrefs ->
        val idNums = mutableMapOf<Long, Int>()
        persons.map(Person::id).forEach {  idNums[it!!] = 0 }

        crossrefs.forEach {
            if (it.second in idNums.keys) idNums[it.second] = idNums[it.second]!! + 1
            else idNums[it.second] = 1
        }

        idNums
    }.flatMapMerge { idNums ->
        val persons = idNums.map {
            personFromId(it.key)
        }
        if (persons.isEmpty()) return@flatMapMerge flowOf(emptyList())

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