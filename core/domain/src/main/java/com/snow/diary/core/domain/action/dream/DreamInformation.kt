package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.location.LocationsFromDream
import com.snow.diary.core.domain.action.person.PersonWithRelationAct
import com.snow.diary.core.domain.action.person.PersonsFromDream
import com.snow.diary.core.model.combine.PersonWithRelation
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf

class DreamInformation(
    val personsFromDream: PersonsFromDream,
    val locationsFromDream: LocationsFromDream,
    val personWithRelation: PersonWithRelationAct,
    val dreamById: DreamById
) : com.snow.diary.core.domain.action.FlowAction<Long, DreamInformation.Output?>() {

    data class Output(

        val dream: Dream,

        val persons: List<PersonWithRelation>,

        val locations: List<Location>
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun Long.createFlow(): Flow<Output?> = dreamById(this).flatMapMerge { dream ->
        if (dream == null)
            return@flatMapMerge flowOf(null)

        personsFromDream(dream)
            .flatMapMerge { persons ->
                combine(
                    flow = locationsFromDream(dream),
                    flow2 = persons.personsWithRelationsFlow()
                ) { locations, pwrs ->
                    Output(dream, pwrs, locations)
                }
            }
    }


    private fun List<Person>.personsWithRelationsFlow(): Flow<List<PersonWithRelation>> =
        if (isEmpty()) flowOf(emptyList()) else
            combine(
                map {
                    personWithRelation(it)
                }
            ) { it.toList() }

}