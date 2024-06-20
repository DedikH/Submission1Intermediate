package com.example.bismillahbisaintermediate.View.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.View.ListStory.ListStory
import com.example.bismillahbisaintermediate.View.Login.Login
import com.example.bismillahbisaintermediate.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    private lateinit var authRepository: UserRepository
    private var authToken: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = APIConfig.postLogin()
        val userPreferenceDataStore = UserPreferenceDataStore(this)

        authRepository = UserRepository(apiService, userPreferenceDataStore)

        Handler(Looper.getMainLooper()).postDelayed({
            checkSession()
        },  600L)

    }

    private fun checkSession(){
        lifecycleScope.launch {
            authToken ?: authRepository.getAuthToken().collect{token ->
                if(token!!.isNotEmpty()){
                    val intent = Intent(this@SplashScreen, ListStory::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@SplashScreen, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                }
            }
        }
    }
}