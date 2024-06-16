package com.example.bismillahbisaintermediate.View.ListStory

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import com.example.bismillahbisaintermediate.databinding.ActivityListStoryBinding
import com.example.bismillahbisaintermediate.databinding.ActivityLoginBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ListStory : AppCompatActivity() {
    private lateinit var binding : ActivityListStoryBinding
    private lateinit var authRepository: UserRepository
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authToken = intent.getStringExtra("token")

        // Initialize the dependencies
        val apiService = APIConfig.postLogin() // Assuming APIConfig.create() initializes APIServices
        val userPreferenceDataStore = UserPreferenceDataStore(this) // Pass context if required

        authRepository = UserRepository(apiService, userPreferenceDataStore)
        StoryHandler()
    }
    private fun StoryHandler() {
        showLoading(true)
        lifecycleScope.launch {
            val token = authToken ?: authRepository.getAuthToken().first()
            try {
                val client = APIConfig.ListStory(token.toString()).getStoriesAll(1, 20)

                client.enqueue(object : Callback<ListStoryResponse> {
                    override fun onResponse(
                        call: Call<ListStoryResponse>,
                        response: Response<ListStoryResponse>
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && responseBody.listStory.isNotEmpty()) {
                                SetListUsers(responseBody.listStory)
                            } else {
                                Log.e(ContentValues.TAG, "No stories found")
                            }
                        } else {
                            Log.e(ContentValues.TAG, "API response error: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                        showLoading(false)
                        Log.e(ContentValues.TAG, "API call failure: ${t.message}")
                    }
                })
            } catch (e: IOException) {
                showLoading(false)
                Log.e(ContentValues.TAG, "IOException: ${e.message}")
            }
        }
    }

    private fun SetListUsers(listStory: List<ListStoryItem>) {
        adapter.submitList(listStory)
        binding.rvListStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun testnew(){
        lifecycleScope.launch {

        }
    }

    companion object {
        private val adapter = storyAdapter()
    }
}