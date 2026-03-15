package com.example.newsapp.presentation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.newsapp.presentation.userpreferences.UserPreferencesRepository
import com.example.newsapp.presentation.userpreferences.UserPreferencesRepositoryImpl
import com.example.newsapp.presentation.userpreferences.userDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule{

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    companion object PreferencesModule {
        @Provides
        @Singleton
        fun provideUserPreferencesRepository(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences>{
            return applicationContext.userDataStore
        }

    }
}



