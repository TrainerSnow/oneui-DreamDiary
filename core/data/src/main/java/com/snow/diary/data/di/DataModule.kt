package com.snow.diary.data.di

import com.snow.diary.data.internal.DatabaseDreamRepo
import com.snow.diary.data.internal.DatabaseLocationRepository
import com.snow.diary.data.internal.DatabasePersonRepo
import com.snow.diary.data.internal.DatabaseRelationRepository
import com.snow.diary.data.repository.DreamRepository
import com.snow.diary.data.repository.LocationRepository
import com.snow.diary.data.repository.PersonRepository
import com.snow.diary.data.repository.RelationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun dreamRepo(
        repo: DatabaseDreamRepo
    ): DreamRepository

    @Binds
    fun locationRepo(
        repo: DatabaseLocationRepository
    ): LocationRepository

    @Binds
    fun personRepo(
        repo: DatabasePersonRepo
    ): PersonRepository

    @Binds
    fun relationRepo(
        repo: DatabaseRelationRepository
    ): RelationRepository

}