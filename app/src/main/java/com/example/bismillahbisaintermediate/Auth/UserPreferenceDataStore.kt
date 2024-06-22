package com.example.bismillahbisaintermediate.Auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserPreferenceDataStore(
    private val context: Context
) {

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
}
    suspend fun ClearSavedToken() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    fun getToken(): Flow<String> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[TOKEN_KEY] ?: ""
            }
    }


    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
        private val TOKEN_KEY = stringPreferencesKey("token_data")
        private var INSTANCE: UserPreferenceDataStore? = null

        fun getInstance(context: Context): UserPreferenceDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferenceDataStore(context)
                INSTANCE = instance
                instance
            }
        }
    }
}