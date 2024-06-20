package com.example.bismillahbisaintermediate.View.ListStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bismillahbisaintermediate.Auth.UserRepository
import kotlinx.coroutines.launch

class ListStoryViewModel(
    private val authRepository: UserRepository
) : ViewModel() {
    fun deleteToken(){
        viewModelScope.launch {
            authRepository.deleteToken()
        }
    }

}