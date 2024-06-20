package com.example.bismillahbisaintermediate.View.Login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bismillahbisaintermediate.API.APIConfig.Companion.postLogin
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Response.LoginResponse
import com.example.bismillahbisaintermediate.View.ListStory.ListStory
import com.example.bismillahbisaintermediate.View.Register.Register
import com.example.bismillahbisaintermediate.ViewModelFactory
import com.example.bismillahbisaintermediate.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var userRepository : UserRepository
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var authRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = postLogin()
        val userPreferenceDataStore = UserPreferenceDataStore(this)

        userRepository = UserRepository(apiService, userPreferenceDataStore)

        val factory = ViewModelFactory(userRepository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        authRepository = UserRepository(apiService, userPreferenceDataStore)

        playAnimation()
        CallError()
        btnLoginClick()

//        checkSession()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.fotologin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val logintxt = ObjectAnimator.ofFloat(binding.titlelogin, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.EmailBlokLogin, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordlogin, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titlelogin, View.ALPHA, 1f).setDuration(100)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val txtregister = ObjectAnimator.ofFloat(binding.registertext, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(email, password)
        }
        AnimatorSet().apply {
            playSequentially(title, btnlogin, txtregister, logintxt, together)
            start()
        }
    }


    //Email Error

    private fun CallError(){
        binding.passwordBlokLogin.Passwordtxt(binding.passwordlogin)
        binding.EmailBlokLogin.emailerror(binding.EmailLogin)
    }



    //Password Error
    private fun passworderror() {
        binding.passwordlogin.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                binding.passwordBlokLogin.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordtxt = binding.passwordlogin.text.toString()
        if(passwordtxt.length < 8){
            return "Password Kurang dari 8 Huruf"
        }else{
            binding.passwordBlokLogin.helperText = ""
        }
        return null
    }
    private fun IntentRegister(){
        binding.registertext.setOnClickListener {
            val i = Intent(this, Register::class.java)
            startActivity(i)
        }
    }
    private fun btnLoginClick(){
        binding.btnLogin.setOnClickListener{
            handleLogin()
        }
    }


    private fun handleLogin() {
        val email = binding.EmailLogin.text.toString().trim()
        val password = binding.passwordlogin.text.toString()

        postLogin().login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Login, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val loginResponse = response.body()!!
                    val token = loginResponse.loginResult.token
                    loginViewModel.saveAuthToken(token)
                    val intent = Intent(this@Login, ListStory::class.java)
                    intent.putExtra("token", token)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login Error", "Network failure: ${t.message}")
            }
        })
    }

}