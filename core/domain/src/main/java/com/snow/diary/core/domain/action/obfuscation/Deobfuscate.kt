package com.snow.diary.core.domain.action.obfuscation;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.model.preferences.ObfuscationPreferences
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao
import kotlinx.coroutines.flow.firstOrNull

class Deobfuscate(
    val obfuscationDao: ObfuscationInfoDao,
    val dreamDao: DreamDao,
    val personDao: PersonDao,
    val locationDao: LocationDao,
    val relationDao: RelationDao
) : Action<ObfuscationPreferences, Boolean>() {
    override suspend fun ObfuscationPreferences.compose(): Boolean {
        val dreamInfos = obfuscationDao.getAllDreamInfos()
        val personInfos = obfuscationDao.getAllPersonInfos()
        val locationInfos = obfuscationDao.getAllLocationInfos()
        val relationInfos = obfuscationDao.getAllRelationInfos()

        val dreams = dreamDao.getAll().firstOrNull() ?: emptyList()
        val persons = personDao.getAll().firstOrNull() ?: emptyList()
        val locations = locationDao.getAll().firstOrNull() ?: emptyList()
        val relations = relationDao.getAll().firstOrNull() ?: emptyList()

        val updatedDreams = dreams.map { dream ->
            val info = dreamInfos.find { it.dreamId == dream.dreamId } ?: return false
            return@map dream.copy(
                description = info.dreamDescription,
                note = info.dreamNote
            )
        }.toTypedArray()
        val updatedPersons = persons.map { person ->
            val info = personInfos.find { it.personId == person.personId } ?: return false
            person.copy(
                name = info.personName,
                notes = info.personNote
            )
        }.toTypedArray()
        val updatedLocations = locations.map { location ->
            val info = locationInfos.find { it.locationId == location.locationId } ?: return false
            location.copy(
                name = info.locationName,
                notes = info.locationNote
            )
        }.toTypedArray()
        val updatedRelations = relations.map { relation ->
            val info = relationInfos.find { it.relationId == relation.relationId } ?: return false
            relation.copy(
                name = info.relationName,
                notes = info.relationNote
            )
        }.toTypedArray()

        val isOk = updatedDreams.size == dreams.size &&
                updatedPersons.size == persons.size &&
                updatedLocations.size == locations.size &&
                updatedRelations.size == relationInfos.size

        if(!isOk) return false

        dreamDao.update(*updatedDreams)
        personDao.update(*updatedPersons)
        locationDao.update(*updatedLocations)
        relationDao.update(*updatedRelations)

        return true
    }
}