package com.example.newsapp.presentation.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


interface UserPreferencesRepository {
    val isDarkTheme : Flow<Boolean>
    suspend fun setDarkTheme(isDark : Boolean)
}

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository{

    companion object{
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }


    //write to data store
    override suspend fun setDarkTheme(isDark : Boolean)
    {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }

    override val isDarkTheme: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME] ?: false
        }

}