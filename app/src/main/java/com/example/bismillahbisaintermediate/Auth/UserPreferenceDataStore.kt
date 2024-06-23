package com.example.bismillahbisaintermediate.Auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bismillahbisaintermediate.DataClass.LoginDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
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

    fun getSession(): Flow<LoginDataClass> {
        return context.dataStore.data.map { preferences ->
            LoginDataClass(
                preferences[TOKEN_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }


    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")
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