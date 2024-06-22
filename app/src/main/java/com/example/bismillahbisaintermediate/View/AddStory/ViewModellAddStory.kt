package com.example.bismillahbisaintermediate.View.AddStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Response.ResponseAddStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class ViewModellAddStory(
    private val authRepository: UserRepository
) : ViewModel() {
    private val story = MutableLiveData<ResponseAddStory>()
    val liveDataStory : LiveData<ResponseAddStory> = story

    suspend fun uploadStoryImage(token: String, multipartBody: MultipartBody.Part, description: RequestBody): ResponseAddStory {
        return withContext(Dispatchers.IO) {
            try {
                val apiService = APIConfig.ListStory(token)
                val response = apiService.UploadStories(multipartBody, description)
                story.postValue(response)
                response
            } catch (e: Exception) {
                throw e
            }
        }
    }
}