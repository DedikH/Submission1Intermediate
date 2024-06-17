package com.example.bismillahbisaintermediate.Auth

import com.example.bismillahbisaintermediate.Response.LoginResponse
import com.example.submission1intermediate.API.APIServices
import com.example.submission1intermediate.API.Response.ResponseRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call

class UserRepository(
    private val apiService: APIServices,
    private val UserPreferenceDataStores : UserPreferenceDataStore
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
}