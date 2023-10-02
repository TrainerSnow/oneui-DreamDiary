package com.snow.diary.core.domain.action.obfuscation

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.database.model.LocationEntity
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.database.model.RelationEntity
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.domain.pure.mapToModel
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.preferences.ObfuscationPreferences
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao
import com.snow.diary.core.obfuscation.db.model.DreamObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.LocationObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.PersonObfuscationInfo
import com.snow.diary.core.obfuscation.db.model.RelationObfuscationInfo
import kotlinx.coroutines.flow.firstOrNull

class Obfuscate(
    val obfuscationDao: ObfuscationInfoDao,
    val dreamDao: DreamDao,
    val personDao: PersonDao,
    val locationDao: LocationDao,
    val relationDao: RelationDao
) : Action<ObfuscationPreferences, Unit>() {
    override suspend fun ObfuscationPreferences.compose() {
        require(obfuscationEnabled) //means right now, normal db should contain proper data

        clearObfuscationDb(obfuscationDao) //clear obfuscation db. Should be empty anyway

        val dreams = dreamDao.getAll().firstOrNull()?.mapToModel() ?: emptyList()
        val persons = personDao.getAll().firstOrNull()?.mapToModel() ?: emptyList()
        val locations = locationDao.getAll().firstOrNull()?.mapToModel() ?: emptyList()
        val relations = relationDao.getAll().firstOrNull()?.mapToModel() ?: emptyList()

        //Get infos and insert before deleting
        obfuscationDao.addDreamInfo(*createDreamObfuscationInfos(dreams))
        obfuscationDao.addPersonIndo(*createPersonObfuscationInfos(persons))
        obfuscationDao.addLocationInfo(*createLocationObfuscationInfos(locations))
        obfuscationDao.addRelationInfo(*createRelationObfuscationInfos(relations))

        //Now 'update' (= obfuscate) data
        if (obfuscateDreams) {
            dreamDao.update(*dreams
                .map { it.obfuscate(true) }
                .map { DreamEntity(it) }
                .toTypedArray())
        }
        if (obfuscatePersons) {
            personDao.update(*persons
                .map { it.obfuscate(true) }
                .map { PersonEntity(it) }
                .toTypedArray())
        }
        if (obfuscateLocations) {
            locationDao.update(*locations
                .map { it.obfuscate(true) }
                .map { LocationEntity(it) }
                .toTypedArray())
        }
        if (obfuscateRelations) {
            relationDao.update(*relations
                .map { it.obfuscate(true) }
                .map { RelationEntity(it) }
                .toTypedArray())
        }
    }

    private fun clearObfuscationDb(obfuscationDao: ObfuscationInfoDao) {
        obfuscationDao.deleteAllDreamInfos()
        obfuscationDao.deleteAllPersonInfos()
        obfuscationDao.deleteAllLocationInfos()
        obfuscationDao.deleteAllRelationInfos()
    }

    private suspend fun createDreamObfuscationInfos(
        dreams: List<Dream>
    ): Array<DreamObfuscationInfo> {
        val infos = dreams.map { dream ->
            DreamObfuscationInfo(
                dreamId = dream.id!!,
                dreamNote = dream.note,
                dreamDescription = dream.description
            )
        }

        return infos.toTypedArray()
    }

    private suspend fun createPersonObfuscationInfos(
        persons: List<Person>
    ): Array<PersonObfuscationInfo> {
        val infos = persons.map { person ->
            PersonObfuscationInfo(
                personId = person.id!!,
                personName = person.name,
                personNote = person.notes
            )
        }

        return infos.toTypedArray()
    }

    private suspend fun createLocationObfuscationInfos(
        locations: List<Location>
    ): Array<LocationObfuscationInfo> {
        val infos = locations.map { location ->
            LocationObfuscationInfo(
                locationId = location.id!!,
                locationName = location.name,
                locationNote = location.notes
            )
        }

        return infos.toTypedArray()
    }

    private suspend fun createRelationObfuscationInfos(
        relations: List<Relation>
    ): Array<RelationObfuscationInfo> {
        val infos = relations.map { relation ->
            RelationObfuscationInfo(
                relationId = relation.id!!,
                relationName = relation.name,
                relationNote = relation.notes
            )
        }

        return infos.toTypedArray()
    }

    private fun Dream.obfuscate(alsoNote: Boolean): Dream = copy(
        description = "Description [$id]",
        note = if (alsoNote) "Note [$id]" else note
    )

    private fun Person.obfuscate(alsoNote: Boolean): Person = copy(
        name = "Name [$id]",
        notes = if (alsoNote) "Note [$id]" else notes
    )

    private fun Location.obfuscate(alsoNote: Boolean): Location = copy(
        name = "Name [$id]",
        notes = if (alsoNote) "Note [$id]" else notes
    )

    private fun Relation.obfuscate(alsoNote: Boolean): Relation = copy(
        name = "Name [$id]",
        notes = if (alsoNote) "Note [$id]" else notes
    )


}