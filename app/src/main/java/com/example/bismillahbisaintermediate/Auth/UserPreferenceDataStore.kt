package com.example.bismillahbisaintermediate.Auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceDataStore(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
}
    fun GetToken() : Flow<String> {
        return dataStore.data.map{
                preferences -> preferences[TOKEN_KEY].toString()
        }
    }
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_data")
    }
}