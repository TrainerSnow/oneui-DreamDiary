package com.snow.diary.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.snow.diary.core.datastore.PreferencesSerializer
import com.snow.diary.core.datastore.UserPreferences
import com.snow.diary.core.datastore.data.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ) =
        DataStoreFactory.create(
            serializer = PreferencesSerializer,
            scope = CoroutineScope(Dispatchers.IO)
        ) {
            context.dataStoreFile("preferences.pb")
        }

    @Provides
    @Singleton
    fun providePreferencesDataSource(
        dataStore: DataStore<UserPreferences>
    ) = PreferencesDataSource(dataStore)


}