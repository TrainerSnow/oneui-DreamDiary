package com.snow.diary.data.internal;

import com.snow.diary.common.map
import com.snow.diary.common.sortedDirectional
import com.snow.diary.data.repository.DreamRepository
import com.snow.diary.database.cross.DreamLocationCrossref
import com.snow.diary.database.cross.DreamPersonCrossref
import com.snow.diary.database.dao.DreamDao
import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.dao.RelationDao
import com.snow.diary.database.model.DreamEntity
import com.snow.diary.database.model.asModel
import com.snow.diary.model.combine.DreamAggregate
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatabaseDreamRepo @Inject constructor(
    val dreamDao: DreamDao,
    val relationDao: RelationDao,
    val locationDao: LocationDao,
    val personDao: PersonDao
) : DreamRepository {
    override suspend fun insert(vararg dream: Dream): List<Long> = dreamDao
        .insert(*dream.map { DreamEntity(it) })

    override suspend fun upsertDreamPersonCrossref(dreamId: Long, personId: Long) = dreamDao
        .upsertDreamPersonCrossref(DreamPersonCrossref(dreamId, personId))

    override suspend fun upsertDreamLocationCrossref(dreamId: Long, locationId: Long) = dreamDao
        .upsertDreamLocationCrossref(DreamLocationCrossref(dreamId, locationId))

    override suspend fun deleteDreamPersonCrossref(dreamId: Long, personId: Long) = dreamDao
        .deleteDreamPersonCrossref(DreamPersonCrossref(dreamId, personId))

    override suspend fun deleteDreamLocationCrossref(dreamId: Long, locationId: Long) = dreamDao
        .deleteDreamLocationCrossref(DreamLocationCrossref(dreamId, locationId))

    override suspend fun update(vararg dream: Dream) = dreamDao
        .update(*dream.map { DreamEntity(it) })

    override suspend fun deleteDream(vararg dream: Dream) = dreamDao
        .delete(*dream.map { DreamEntity(it) })

    override fun getAllDreams(sortConfig: SortConfig): Flow<List<Dream>> = dreamDao
        .getAllDreams()
        .map { dreams ->
            dreams
                .sort(sortConfig)
                .map(DreamEntity::asModel)
        }

    override fun getDreamById(id: Long): Flow<Dream?> = dreamDao
        .getDreamById(id)
        .map { it?.asModel }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getExtendedDreamById(id: Long): Flow<DreamAggregate?> {
        val persons = dreamDao
            .getDreamWithPersonsById(id)
            .map {
                it?.persons ?: emptyList()
            }

        val relations = persons //Getting relations off the persons we got
            .flatMapMerge { personEntities ->
                val flows = personEntities.map { person ->
                    relationDao
                        .getRelationById(person.relationId)
                        .map { it!! }
                }
                if(flows.isEmpty()) return@flatMapMerge flowOf(emptyList())
                combine(
                    flows
                ) {
                    it
                        .toList()
                }
            }

        val personsWithRelations = combine(
            persons,
            relations
        ) { ps, rs -> //Combining persons and relations to PersonWithRelation
            List(ps.size) { index ->
                PersonWithRelation(
                    ps[index].asModel,
                    rs[index].asModel
                )
            }
        }

        val locations = dreamDao
            .getDreamWithLocationsById(id)
            .map {
                it?.locations
                    ?.map { it.asModel } ?: emptyList()
            }

        return combine(
            personsWithRelations,
            locations,
            dreamDao
                .getDreamById(id)
        ) { ps, ls, dream ->
            if (dream == null) null
            else DreamAggregate(
                dream.asModel,
                ps,
                ls
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllExtendedDreams(sortConfig: SortConfig): Flow<List<DreamAggregate>> = dreamDao
        .getAllDreams()
        .map { it.sort(sortConfig) }
        .flatMapMerge {
            combine(
                it.map {
                    getExtendedDreamById(it.dreamId!!)
                }
            ) {
                it
                    .toList()
                    .filterNotNull()
            }
        }

    override fun getDreamsByLocation(id: Long): Flow<List<Dream>?> = locationDao
        .getLocationWithDreamsById(id)
        .map {
            it
                ?.dreams
                ?.map { it.asModel }
        }

    override fun getDreamsByPerson(id: Long): Flow<List<Dream>?> = personDao
    .getPersonWithDreamsById(id)
    .map {
        it
            ?.dreams
            ?.map { it.asModel }
    }
}

private fun List<DreamEntity>.sort(sortConfig: SortConfig): List<DreamEntity> =
    when (sortConfig.mode) {
        SortMode.Created -> sortedBy { it.created }
        SortMode.Updated -> sortedBy { it.updated }
        SortMode.Alphabetically -> sortedBy { it.description }
        SortMode.Happiness -> sortedBy { it.happiness ?: 0F }
        SortMode.Clearness -> sortedBy { it.clearness ?: 0F }
        SortMode.Length -> sortedBy { it.description.length }
        else -> this
    }.sortedDirectional(sortConfig.direction)