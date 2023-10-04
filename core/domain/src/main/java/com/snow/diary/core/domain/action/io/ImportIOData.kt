package com.snow.diary.core.domain.action.io;

import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.domain.action.Action
import com.snow.diary.core.domain.action.cross.dream_location.AddDreamLocationCrossref
import com.snow.diary.core.domain.action.cross.dream_person.AddDreamPersonCrossref
import com.snow.diary.core.domain.action.cross.person_relation.AddPersonRelationCrossref
import com.snow.diary.core.domain.action.dream.AddDreamAction
import com.snow.diary.core.domain.action.location.AddLocation
import com.snow.diary.core.domain.action.person.AddPerson
import com.snow.diary.core.domain.action.relation.AddRelation
import com.snow.diary.core.io.data.IOData

class ImportIOData(
    private val addDream: AddDreamAction,
    private val addPerson: AddPerson,
    private val addLocation: AddLocation,
    private val addRelation: AddRelation,
    private val addDreamPersonCrossref: AddDreamPersonCrossref,
    private val addDreamLocationCrossref: AddDreamLocationCrossref,
    private val addPersonRelationCrossref: AddPersonRelationCrossref,
    private val crossrefDao: CrossrefDao,
    private val dreamDao: DreamDao,
    private val personDao: PersonDao,
    private val locationDao: LocationDao,
    private val relationDao: RelationDao
) : Action<IOData, List<ImportIOData.ImportProblem>>() {

    sealed class ImportProblem {

        data object InvalidCrossrefReference : ImportProblem()

        data object NonUniqueIds : ImportProblem()


    }

    override suspend fun IOData.compose(): List<ImportProblem> {
        val problems = mutableListOf<ImportProblem>()

        val dreamIdsUnique = dreams.map { it.id }.toSet().size == dreams.size
        val personIdsUnique = persons.map { it.id }.toSet().size == persons.size
        val locationIdsUnique = locations.map { it.id }.toSet().size == locations.size
        val relationIdsUnique = relations.map { it.id }.toSet().size == relations.size
        val unique = listOf(dreamIdsUnique, personIdsUnique, locationIdsUnique, relationIdsUnique)
        if (!unique.all { it }) {
            problems.add(ImportProblem.NonUniqueIds)
        }

        val dreamPersonCrossrefsOkay = dreamPersonCrossrefs.all {
            it.first in dreams.map { it.id } &&
                    it.second in persons.map { it.id }
        }
        val dreamLocationCrossrefsOkay = dreamLocationsCrossrefs.all {
            it.first in dreams.map { it.id } &&
                    it.second in locations.map { it.id }
        }
        val personRelationCrossrefsOkay = personRelationCrossrefs.all {
            it.first in persons.map { it.id } &&
                    it.second in relations.map { it.id }
        }
        val crossrefsOkay = listOf(
            dreamPersonCrossrefsOkay,
            dreamLocationCrossrefsOkay,
            personRelationCrossrefsOkay
        )
        if (!crossrefsOkay.all { it }) {
            problems.add(ImportProblem.InvalidCrossrefReference)
        }

        if (problems.isNotEmpty()) return problems

        //Deleting all old entries
        crossrefDao.apply {
            deleteAllPersonRelationCrossrefs()
            deleteAllDreamLocationCrossrefs()
            deleteAllDreamPersonCrossrefs()
        }
        dreamDao.deleteAll()
        personDao.deleteAll()
        locationDao.deleteAll()
        relationDao.deleteAll()

        //Inserting new
        addDream(dreams)
        persons.forEach { addPerson(it) }
        locations.forEach { addLocation(it) }
        relations.forEach { addRelation(it) }

        dreamPersonCrossrefs.forEach {
            addDreamPersonCrossref(AddDreamPersonCrossref.Input(it.first, it.second))
        }
        dreamLocationsCrossrefs.forEach {
            addDreamLocationCrossref(AddDreamLocationCrossref.Input(it.first, it.second))
        }
        personRelationCrossrefs.forEach {
            addPersonRelationCrossref(AddPersonRelationCrossref.Input(it.first, it.second))
        }

        return emptyList()
    }

}