package com.example.bismillahbisaintermediate.View.Login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Credentials
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.API.APIConfig.Companion.postLogin
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.Response.LoginResponse
import com.example.bismillahbisaintermediate.View.ListStory.ListStory
import com.example.bismillahbisaintermediate.View.Register.Register
import com.example.bismillahbisaintermediate.ViewModelFactory
import com.example.bismillahbisaintermediate.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var userRepository : UserRepository
    private lateinit var loginViewModel : LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the dependencies
        val apiService = postLogin() // Assuming APIConfig.create() initializes APIServices
        val userPreferenceDataStore = UserPreferenceDataStore(this) // Pass context if required

        userRepository = UserRepository(apiService, userPreferenceDataStore)

        val factory = ViewModelFactory(userRepository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        emailerror()
        passworderror()
        IntentRegister()
        playAnimation()
        btnLoginClick()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.fotologin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val logintxt = ObjectAnimator.ofFloat(binding.titlelogin, View.ALPHA, 1f).setDuration(100)
        val emailtxt = ObjectAnimator.ofFloat(binding.txtemail, View.ALPHA, 1f).setDuration(100)
        val passwordtxt = ObjectAnimator.ofFloat(binding.txtpassword, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.EmailBlokLogin, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordBlokLogin, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titlelogin, View.ALPHA, 1f).setDuration(100)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val txtregister = ObjectAnimator.ofFloat(binding.registertext, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(email, password)
        }
        AnimatorSet().apply {
            playSequentially(title, btnlogin, txtregister, logintxt, emailtxt, passwordtxt, together)
            start()
        }
    }


    //Email Error
    private fun emailerror() {
        binding.EmailLogin.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                binding.EmailBlokLogin.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailtxt = binding.EmailLogin.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches()){
            return "Email Invalid"
        }
        return null
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
                    AlertDialog.Builder(this@Login).apply {
                        setTitle("Login Succes!")
                        setMessage("Success")
                        setPositiveButton("Lanjut") { _, _ ->
                            val loginResponse = response.body()!!
                            val token = loginResponse.loginResult.token
                            loginViewModel.saveAuthToken(token)
                            val intent = Intent(context, ListStory::class.java)
                            intent.putExtra("token", token)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    AlertDialog.Builder(this@Login).apply {
                        setTitle("Login Failed!")
                        setMessage("Try Again")
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(context, Login::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login Error", "Network failure: ${t.message}")
            }
        })
    }
}