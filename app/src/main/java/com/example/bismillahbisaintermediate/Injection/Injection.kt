package com.example.bismillahbisaintermediate.Injection

import android.content.Context
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Database.ListStoryDB


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userRepository = UserPreferenceDataStore.getInstance(context)
        val database = ListStoryDB.getDatabase(context)
        val user =  userRepository.getToken()
        val apiService = APIConfig.ListStory(user.toString())
        return UserRepository(apiService, userRepository ,database)
    }
}