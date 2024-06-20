package com.example.bismillahbisaintermediate.View.ListStory

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.Response.ListStoryItem
import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import com.example.bismillahbisaintermediate.View.AddStory.AddStory
import com.example.bismillahbisaintermediate.View.DetailStory.DetailStory
import com.example.bismillahbisaintermediate.View.Login.Login
import com.example.bismillahbisaintermediate.View.Login.LoginViewModel
import com.example.bismillahbisaintermediate.ViewModelFactory
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

class ListStory : AppCompatActivity(), RVonclick {
    private lateinit var binding : ActivityListStoryBinding
    private lateinit var authRepository: UserRepository
    private lateinit var listStoryViewModel : ListStoryViewModel
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authToken = intent.getStringExtra("token")

        // Initialize the dependencies
        val apiService = APIConfig.postLogin()
        val userPreferenceDataStore = UserPreferenceDataStore(this)
        authRepository = UserRepository(apiService, userPreferenceDataStore)
        val factory = ViewModelFactory(authRepository)
        listStoryViewModel = ViewModelProvider(this, factory).get(ListStoryViewModel::class.java)
        binding.rvListStory.layoutManager = LinearLayoutManager(this)

        StoryHandler()
        intentdetail()
        appbar()
        intentAdd()
    }

    private fun StoryHandler() {
        showLoading(false)
        lifecycleScope.launch {
            val token = authToken ?: authRepository.getAuthToken().first()
            try {
                val client = APIConfig.ListStory(token.toString()).getStoriesAll()

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
    private fun intentAdd(){
        binding.AddStory.setOnClickListener{
            val intent = Intent(this, AddStory::class.java)
            startActivity(intent)
        }
    }

    private fun intentdetail(){
        adapter.listener = this
    }
    override fun onItemClicked(view : View, listStory: ListStoryItem){
        val intents = Intent(this, DetailStory::class.java)
        val bundle = Bundle()
        intents.putExtra("title", listStory.name)
        intents.putExtra("description", listStory.description)
        bundle.putString("imageUrl",listStory.photoUrl)
        intents.putExtras(bundle)
        startActivity(intents)
    }

    private fun appbar(){
        var toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                logouthandle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

        private fun logouthandle(){
            lifecycleScope.launch {
                listStoryViewModel.deleteToken()
            }
            finishAffinity()
        }

        companion object {
        private val adapter = storyAdapter()
        }
    }
