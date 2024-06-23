package com.example.bismillahbisaintermediate.Injection

import android.content.Context
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Database.ListStoryDB
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userRepository = UserPreferenceDataStore.getInstance(context)
        val database = ListStoryDB.getDatabase(context)
        val user =  runBlocking {  userRepository.getSession().first() }
        val apiService = APIConfig.ListStory(user.token)
        return UserRepository(apiService, userRepository ,database)
    }
}