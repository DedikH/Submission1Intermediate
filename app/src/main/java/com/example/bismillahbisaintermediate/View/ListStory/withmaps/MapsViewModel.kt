package com.example.bismillahbisaintermediate.View.ListStory.withmaps


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel (
    private val authRepository: UserRepository,
) : ViewModel(){
    private val _storyLiveData = MutableLiveData<ListStoryResponse>()
    val stories: LiveData<ListStoryResponse> = _storyLiveData


    fun MarkerwithLocation() {
        viewModelScope.launch {
            try {
                val response = authRepository.ListStoryinMap()
                _storyLiveData.postValue(response)
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    "Error Please try 401"
                }
            } catch (e: Exception) {
            }
        }
        }
    }