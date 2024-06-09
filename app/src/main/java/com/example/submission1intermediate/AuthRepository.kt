package com.example.submission1intermediate

import com.example.submission1intermediate.API.APIServices
import com.example.submission1intermediate.API.Response.LoginResponse
import com.example.submission1intermediate.API.Response.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService : APIServices,
    private val PrefDataStore : AuthPref
) {
    suspend fun saveAuthToken(token: String) {
        PrefDataStore.saveAuthToken(token)
    }

    fun getAuthToken(token: String): Flow<String?> = PrefDataStore.getAuthToken()

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
