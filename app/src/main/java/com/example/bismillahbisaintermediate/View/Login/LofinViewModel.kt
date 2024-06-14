package com.example.bismillahbisaintermediate.View.Login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginViewModel(
    private val authRepository: UserRepository
) : ViewModel() {
    suspend fun userLogin(email: String, password: String): Flow<Result<Call<LoginResponse>>> =
        authRepository.userLogin(email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }
}