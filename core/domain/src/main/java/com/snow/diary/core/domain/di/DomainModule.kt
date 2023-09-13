package com.snow.diary.core.domain.di

import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.dao.PersonDao
import com.snow.diary.core.database.dao.RelationDao
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
import com.snow.diary.core.domain.action.person.AddPerson
import com.snow.diary.core.domain.action.person.AllPersons
import com.snow.diary.core.domain.action.person.DeletePerson
import com.snow.diary.core.domain.action.person.PersonFromId
import com.snow.diary.core.domain.action.person.PersonWithRelationsAct
import com.snow.diary.core.domain.action.person.PersonsFromDream
import com.snow.diary.core.domain.action.person.PersonsFromRelation
import com.snow.diary.core.domain.action.person.PersonsWithRelationsAct
import com.snow.diary.core.domain.action.person.UpdatePerson
import com.snow.diary.core.domain.action.relation.AddRelation
import com.snow.diary.core.domain.action.relation.AllRelations
import com.snow.diary.core.domain.action.relation.DeleteRelation
import com.snow.diary.core.domain.action.relation.RelationById
import com.snow.diary.core.domain.action.relation.RelationWithPersonsAct
import com.snow.diary.core.domain.action.relation.RelationsWithPersonsAct
import com.snow.diary.core.domain.action.relation.UpdateRelation
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
        dreamDao: DreamDao
    ) = DeleteDream(dreamDao)


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
        locationDao: LocationDao
    ) = DeleteLocation(locationDao)



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
        personDao: PersonDao
    ) = DeletePerson(personDao)

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
        relationDao: RelationDao
    ) = DeleteRelation(relationDao)

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
        crossrefDao: CrossrefDao
    ) = AllDreamPersonCrossrefs(crossrefDao)

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

}