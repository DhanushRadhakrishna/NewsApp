package com.example.newsapp.presentation.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context


val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")