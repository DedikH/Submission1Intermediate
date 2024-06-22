package com.example.bismillahbisaintermediate.View.ListStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ListStoryViewModel(
    private val authRepository: UserRepository
) : ViewModel() {
    fun deleteToken(){
        viewModelScope.launch {
            authRepository.deleteToken()
        }
    }
    fun getStories(): Flow<PagingData<ListStoryItem>> {
        return authRepository.getUnlList().cachedIn(viewModelScope)
    }

}