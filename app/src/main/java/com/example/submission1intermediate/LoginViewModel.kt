package com.example.submission1intermediate

import androidx.lifecycle.ViewModel
import com.example.submission1intermediate.API.Response.LoginResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun userLogin(email : String, password : String) : Flow<Result<Call<LoginResponse>>> =
        authRepository.userLogin(email, password)

    fun savetoken(token : String){
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}