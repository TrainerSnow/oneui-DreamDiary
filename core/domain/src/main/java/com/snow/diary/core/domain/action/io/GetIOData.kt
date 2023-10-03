package com.snow.diary.core.domain.action.io;

import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.FlowAction
import com.snow.diary.core.domain.action.cross.dream_location.AllDreamLocationCrossrefs
import com.snow.diary.core.domain.action.cross.dream_person.AllDreamPersonCrossrefs
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.relation.AllRelations
import com.snow.diary.core.io.data.IOData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetIOData(
    private val allDreams: AllDreams,
    private val allPersons: AllPersons,
    private val allLocations: AllLocations,
    private val allRelations: AllRelations,
    private val allDreamPersonCrossrefs: AllDreamPersonCrossrefs,
    private val allDreamLocationCrossrefs: AllDreamLocationCrossrefs,
    private val crossrefDao: CrossrefDao
) : FlowAction<Unit, IOData>() {

    override fun Unit.createFlow(): Flow<IOData> = combine(
        flow = allDreams(AllDreams.Input()),
        flow2 = allPersons(AllPersons.Input()),
        flow3 = allLocations(AllLocations.Input()),
        flow4 = allRelations(AllRelations.Input()),
        flow5 = allDreamPersonCrossrefs(AllDreamPersonCrossrefs.Input()),
        flow6 = allDreamLocationCrossrefs(AllDreamLocationCrossrefs.Input()),
        flow7 = crossrefDao.getAllPersonRelationCrossrefs()
    ) { dreams, persons, locations, relations, dreamPersonCrossrefs, dreamLocationCrossrefs, personRelationCrossrefs ->
        IOData(
            dreams, persons, locations, relations,
            dreamPersonCrossrefs,
            dreamLocationCrossrefs,
            personRelationCrossrefs.map { Pair(it.personId, it.relationId) }
        )
    }

}

@Suppress("UNCHECKED_CAST")
private fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = combine(flow, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
        args[6] as T7
    )
}