package com.example.bismillahbisaintermediate.Auth

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.bismillahbisaintermediate.Database.ListStoryDB
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import com.example.bismillahbisaintermediate.Response.LoginResponse
import com.example.bismillahbisaintermediate.View.ListStory.paging.PagingSource
import com.example.submission1intermediate.API.APIServices
import com.example.submission1intermediate.API.Response.ResponseRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call

class UserRepository(
    private val apiService: APIServices,
    private val UserPreferenceDataStores: UserPreferenceDataStore,
    private val storyDatabase: ListStoryDB
) {

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<Call<ResponseRegister>>> = flow {
        try {
            val response = apiService.register(name, email, password)
            emit(Result.success(response))
        }catch(e : Exception){

        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        UserPreferenceDataStores.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?>{
        return UserPreferenceDataStores.getToken()
    }

    suspend fun deleteToken() {
        UserPreferenceDataStores.ClearSavedToken()
    }

    suspend fun userLogin(email: String, password: String): Flow<Result<Call<LoginResponse>>> = flow {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun ListStoryinMap(): ListStoryResponse {
        val token = UserPreferenceDataStores.getToken().firstOrNull()
        return apiService.getStoriesWithLocation("Bearer $token", location = 1)
    }


    fun getUnlList(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PagingSource(apiService)
            }
        ).flow
    }

}