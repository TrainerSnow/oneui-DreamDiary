package com.snow.diary.core.domain.di

import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
import com.snow.diary.core.datastore.data.PreferencesDataSource
import com.snow.diary.core.domain.action.cross.dream_location.AddDreamLocationCrossref
import com.snow.diary.core.domain.action.cross.dream_location.AllDreamLocationCrossrefs
import com.snow.diary.core.domain.action.cross.dream_location.RemoveDreamLocationCrossref
import com.snow.diary.core.domain.action.cross.dream_person.AddDreamPersonCrossref
import com.snow.diary.core.domain.action.cross.dream_person.AllDreamPersonCrossrefs
import com.snow.diary.core.domain.action.cross.dream_person.RemoveDreamPersonCrossref
import com.snow.diary.core.domain.action.cross.person_relation.AddPersonRelationCrossref
import com.snow.diary.core.domain.action.cross.person_relation.AllPersonRelationCrossrefs
import com.snow.diary.core.domain.action.cross.person_relation.RemovePersonRelationCrossref
import com.snow.diary.core.domain.action.dream.AddDreamAction
import com.snow.diary.core.domain.action.dream.AllDreams
import com.snow.diary.core.domain.action.dream.DeleteDream
import com.snow.diary.core.domain.action.dream.DreamById
import com.snow.diary.core.domain.action.dream.DreamInformation
import com.snow.diary.core.domain.action.dream.DreamsFromLocation
import com.snow.diary.core.domain.action.dream.DreamsFromPerson
import com.snow.diary.core.domain.action.dream.UpdateDream
import com.snow.diary.core.domain.action.location.AddLocation
import com.snow.diary.core.domain.action.location.AllLocations
import com.snow.diary.core.domain.action.location.DeleteLocation
import com.snow.diary.core.domain.action.location.LocationById
import com.snow.diary.core.domain.action.location.LocationsFromDream
import com.snow.diary.core.domain.action.location.UpdateLocation
import com.snow.diary.core.domain.action.obfuscation.Deobfuscate
import com.snow.diary.core.domain.action.obfuscation.Obfuscate
import com.snow.diary.core.domain.action.person.AddPerson
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.person.DeletePerson
import com.snow.diary.core.domain.action.person.PersonFromId
import com.snow.diary.core.domain.action.person.PersonWithRelationsAct
import com.snow.diary.core.domain.action.person.PersonsFromDream
import com.snow.diary.core.domain.action.person.PersonsFromRelation
import com.snow.diary.core.domain.action.person.PersonsWithRelationsAct
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.domain.action.preferences.UpdateColorMode
import com.snow.diary.core.domain.action.preferences.UpdateObfuscationPreferences
import com.snow.diary.core.domain.action.preferences.UpdateRequireAuth
import com.snow.diary.core.domain.action.relation.AddRelation
import com.snow.diary.core.domain.action.relation.AllRelations
import com.snow.diary.core.domain.action.relation.DeleteRelation
import com.snow.diary.core.domain.action.relation.RelationById
import com.snow.diary.core.domain.action.relation.RelationWithPersonsAct
import com.snow.diary.core.domain.action.relation.RelationsWithPersonsAct
import com.snow.diary.core.domain.action.relation.UpdateRelation
import com.snow.diary.core.domain.action.statistics.ClearnessAverage
import com.snow.diary.core.domain.action.statistics.DreamAmountAverage
import com.snow.diary.core.domain.action.statistics.DreamAmounts
import com.snow.diary.core.domain.action.statistics.HappinessAverage
import com.snow.diary.core.domain.action.statistics.PersonsWithAmount
import com.snow.diary.core.domain.action.time.FirstDreamDate
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainModule {


    /*
    Dream usecases
     */

    @Provides
    @Singleton
    fun provideAllDreams(
        dreamDao: DreamDao
    ) = AllDreams(dreamDao)


    @Provides
    @Singleton
    fun provideAddDream(
        dreamDao: DreamDao
    ) = AddDreamAction(dreamDao)


    @Provides
    @Singleton
    fun provideDeleteDream(
        dreamDao: DreamDao,
        obfuscationDao: ObfuscationInfoDao
    ) = DeleteDream(dreamDao, obfuscationDao)


    @Provides
    @Singleton
    fun provideUpdateDream(
        dreamDao: DreamDao
    ) = UpdateDream(dreamDao)

    @Provides
    @Singleton
    fun provideDreamById(
        dreamDao: DreamDao
    ) = DreamById(dreamDao)


    @Provides
    @Singleton
    fun provideDreamInformation(
        personsFromDream: PersonsFromDream,
        locationsFromDream: LocationsFromDream,
        personWithRelations: PersonWithRelationsAct,
        dreamById: DreamById
    ) = DreamInformation(personsFromDream, locationsFromDream, personWithRelations, dreamById)

    @Provides
    @Singleton
    fun provideDreamsFromPerson(
        personDao: PersonDao
    ) = DreamsFromPerson(personDao)

    @Provides
    @Singleton
    fun provideDreamsFromLocation(
        locationDao: LocationDao
    ) = DreamsFromLocation(locationDao)


    /*
    Location usecases
     */

    @Provides
    @Singleton
    fun provideLocationsFromDream(
        dreamDao: DreamDao
    ) = LocationsFromDream(dreamDao)


    @Provides
    @Singleton
    fun provideAllLocations(
        locationDao: LocationDao
    ) = AllLocations(locationDao)


    @Provides
    @Singleton
    fun provideLocationById(
        locationDao: LocationDao
    ) = LocationById(locationDao)


    @Provides
    @Singleton
    fun provideAddLocation(
        locationDao: LocationDao
    ) = AddLocation(locationDao)


    @Provides
    @Singleton
    fun provideUpdateLocation(
        locationDao: LocationDao
    ) = UpdateLocation(locationDao)


    @Provides
    @Singleton
    fun provideDeleteLocation(
        locationDao: LocationDao,
        obfuscationDao: ObfuscationInfoDao
    ) = DeleteLocation(locationDao, obfuscationDao)


    /*
    Person usecases
     */

    @Provides
    @Singleton
    fun provideAllPersons(
        personDao: PersonDao
    ) = AllPersons(personDao)


    @Provides
    @Singleton
    fun providePersonsFromDream(
        dreamDao: DreamDao
    ) = PersonsFromDream(dreamDao)


    @Provides
    @Singleton
    fun providePersonWithRelationsAct(
        personDao: PersonDao
    ) = PersonWithRelationsAct(personDao)


    @Provides
    @Singleton
    fun providePersonsWithRelationsAct(
        personDao: PersonDao
    ) = PersonsWithRelationsAct(personDao)


    @Provides
    @Singleton
    fun provideUpdatePerson(
        personDao: PersonDao
    ) = UpdatePerson(personDao)

    @Provides
    @Singleton
    fun providePersonFromId(
        personDao: PersonDao
    ) = PersonFromId(personDao)

    @Provides
    @Singleton
    fun provideDeletePerson(
        personDao: PersonDao,
        obfuscationDao: ObfuscationInfoDao
    ) = DeletePerson(personDao, obfuscationDao)

    @Provides
    @Singleton
    fun provideAddPerson(
        personDao: PersonDao
    ) = AddPerson(personDao)

    @Provides
    @Singleton
    fun providePersonsFromRelation(
        relationDao: RelationDao
    ) = PersonsFromRelation(relationDao)


    /*
    Relation usecases
     */

    @Provides
    @Singleton
    fun provideRelationById(
        relationDao: RelationDao
    ) = RelationById(relationDao)

    @Provides
    @Singleton
    fun provideAlLRelations(
        relationDao: RelationDao
    ) = AllRelations(relationDao)

    @Provides
    @Singleton
    fun provideAddRelation(
        relationDao: RelationDao
    ) = AddRelation(relationDao)

    @Provides
    @Singleton
    fun provideUpdateRelation(
        relationDao: RelationDao
    ) = UpdateRelation(relationDao)

    @Provides
    @Singleton
    fun provideDeleteRelation(
        relationDao: RelationDao,
        obfuscationDao: ObfuscationInfoDao
    ) = DeleteRelation(relationDao, obfuscationDao)

    @Provides
    @Singleton
    fun provideRelationWithPersonsAct(
        relationDao: RelationDao
    ) = RelationWithPersonsAct(relationDao)

    @Provides
    @Singleton
    fun provideRelationsWithPersonsAct(
        relationDao: RelationDao
    ) = RelationsWithPersonsAct(relationDao)


    /*
    Crossref usecases
     */


    @Provides
    @Singleton
    fun provideAddDreamLocationCrossref(
        crossrefDao: CrossrefDao
    ) = AddDreamLocationCrossref(crossrefDao)


    @Provides
    @Singleton
    fun provideAddDreamPersonCrossref(
        crossrefDao: CrossrefDao
    ) = AddDreamPersonCrossref(crossrefDao)


    @Provides
    @Singleton
    fun provideAddPersonRelationCrossref(
        crossrefDao: CrossrefDao
    ) = AddPersonRelationCrossref(crossrefDao)


    @Provides
    @Singleton
    fun provideRemoveDreamLocationCrossref(
        crossrefDao: CrossrefDao
    ) = RemoveDreamLocationCrossref(crossrefDao)


    @Provides
    @Singleton
    fun provideRemoveDreamPersonCrossref(
        crossrefDao: CrossrefDao
    ) = RemoveDreamPersonCrossref(crossrefDao)


    @Provides
    @Singleton
    fun provideRemovePersonRelationCrossref(
        crossrefDao: CrossrefDao
    ) = RemovePersonRelationCrossref(crossrefDao)

    @Provides
    @Singleton
    fun provideAllDreamPersonCrossrefs(
        crossrefDao: CrossrefDao,
        allDreams: AllDreams
    ) = AllDreamPersonCrossrefs(crossrefDao, allDreams)

    @Provides
    @Singleton
    fun provideAllDreamLocationsCrossrefs(
        crossrefDao: CrossrefDao
    ) = AllDreamLocationCrossrefs(crossrefDao)

    @Provides
    @Singleton
    fun provideAllPersonRelationCrossrefs(
        crossrefDao: CrossrefDao
    ) = AllPersonRelationCrossrefs(crossrefDao)


    /*
    Preferences usecases
     */

    @Provides
    @Singleton
    fun provideGetPreferences(
        prefsDataSource: PreferencesDataSource
    ) = GetPreferences(prefsDataSource)

    @Provides
    @Singleton
    fun provideUpdateColorMode(
        prefsDataSource: PreferencesDataSource
    ) = UpdateColorMode(prefsDataSource)

    @Provides
    @Singleton
    fun provideUpdateSecurityMode(
        prefsDataSource: PreferencesDataSource
    ) = UpdateRequireAuth(prefsDataSource)

    @Provides
    @Singleton
    fun provideUpdateObfuscationPreferences(
        prefsDataSource: PreferencesDataSource
    ) = UpdateObfuscationPreferences(prefsDataSource)


    /*
    Obfuscation
     */

    @Provides
    @Singleton
    fun provideObfuscate(
        obfuscationDao: ObfuscationInfoDao,
        dreamDao: DreamDao,
        personDao: PersonDao,
        locationDao: LocationDao,
        relationDao: RelationDao
    ) = Obfuscate(obfuscationDao, dreamDao, personDao, locationDao, relationDao)

    @Provides
    @Singleton
    fun provideDeOfuscate(
        obfuscationDao: ObfuscationInfoDao,
        dreamDao: DreamDao,
        personDao: PersonDao,
        locationDao: LocationDao,
        relationDao: RelationDao
    ) = Deobfuscate(obfuscationDao, dreamDao, personDao, locationDao, relationDao)


    /*
    Time usecases
     */
    @Provides
    @Singleton
    fun provideFirstDreamDate(
        allDreams: AllDreams
    ) = FirstDreamDate(allDreams)

    /*
    Statistics usecases
     */
    @Provides
    @Singleton
    fun provideDreamAmounts(
        allDreams: AllDreams,
        firstDreamDate: FirstDreamDate
    ) = DreamAmounts(allDreams, firstDreamDate)

    @Provides
    @Singleton
    fun provideDreamAmountAverage(
        allDreams: AllDreams
    ) = DreamAmountAverage(allDreams)

    @Provides
    @Singleton
    fun provideHappinessAverage(
        allDreams: AllDreams
    ) = HappinessAverage(allDreams)

    @Provides
    @Singleton
    fun provideClearnessAverage(
        allDreams: AllDreams
    ) = ClearnessAverage(allDreams)

    @Provides
    @Singleton
    fun provideUsedPersons(
        allPersons: AllPersons,
        allDreamPersonCrossrefs: AllDreamPersonCrossrefs,
        personById: PersonFromId
    ) = PersonsWithAmount(allPersons, allDreamPersonCrossrefs, personById)

}