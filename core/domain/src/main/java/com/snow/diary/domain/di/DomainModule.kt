package com.snow.diary.domain.di

import com.snow.diary.database.dao.CrossrefDao
import com.snow.diary.database.dao.DreamDao
import com.snow.diary.database.dao.LocationDao
import com.snow.diary.database.dao.PersonDao
import com.snow.diary.database.dao.RelationDao
import com.snow.diary.domain.action.cross.AddDreamLocationCrossref
import com.snow.diary.domain.action.cross.AddDreamPersonCrossref
import com.snow.diary.domain.action.cross.RemoveDreamLocationCrossref
import com.snow.diary.domain.action.cross.RemoveDreamPersonCrossref
import com.snow.diary.domain.action.dream.AddDreamAction
import com.snow.diary.domain.action.dream.AllDreams
import com.snow.diary.domain.action.dream.DeleteDream
import com.snow.diary.domain.action.dream.DreamById
import com.snow.diary.domain.action.dream.DreamInformation
import com.snow.diary.domain.action.dream.UpdateDream
import com.snow.diary.domain.action.location.AllLocations
import com.snow.diary.domain.action.location.LocationsFromDream
import com.snow.diary.domain.action.person.AllPersons
import com.snow.diary.domain.action.person.PersonWithRelationAct
import com.snow.diary.domain.action.person.PersonsFromDream
import com.snow.diary.domain.action.person.UpdatePerson
import com.snow.diary.domain.action.relation.RelationById
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
        personWithRelation: PersonWithRelationAct,
        dreamById: DreamById
    ) = DreamInformation(personsFromDream, locationsFromDream, personWithRelation, dreamById)


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
    fun providePersonWithRelationAct(
        relationById: RelationById
    ) = PersonWithRelationAct(relationById)


    @Provides
    @Singleton
    fun provideUpdatePerson(
        personDao: PersonDao
    ) = UpdatePerson(personDao)


    /*
    Relation usecases
     */

    @Provides
    @Singleton
    fun provideRelationById(
        relationDao: RelationDao
    ) = RelationById(relationDao)


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
    fun provideRemoveDreamLocationCrossref(
        crossrefDao: CrossrefDao
    ) = RemoveDreamLocationCrossref(crossrefDao)


    @Provides
    @Singleton
    fun provideRemoveDreamPersonCrossref(
        crossrefDao: CrossrefDao
    ) = RemoveDreamPersonCrossref(crossrefDao)

}