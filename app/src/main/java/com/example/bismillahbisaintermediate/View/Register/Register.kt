package com.example.bismillahbisaintermediate.View.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.SharedPrefManager
import com.example.bismillahbisaintermediate.databinding.ActivityRegisterBinding
import com.example.submission1intermediate.API.Response.ResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailerror()
        passworderror()
        playAnimation()
        btnRegister()
    }

    private fun btnRegister(){
        val button = binding.btnRegister
        button.setOnClickListener{
            RegisterUser()
        }
    }

    //Email Error
    private fun emailerror() {
        binding.EmailRegister.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                binding.EmailBlokRegister.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val emailtxt = binding.EmailRegister.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches()){
            return "Email Invalid"
        }
        return null
    }

    //Password Error
    private fun passworderror() {
        binding.passwordRegister.setOnFocusChangeListener{ _, focused ->
            if(!focused){
                binding.passwordBlokRegister.helperText = validPassword()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.fotoregister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleregister, View.ALPHA, 1f).setDuration(100)
        val emailtxt = ObjectAnimator.ofFloat(binding.emailtitleregister, View.ALPHA, 1f).setDuration(100)
        val passwordtxt = ObjectAnimator.ofFloat(binding.passwordtitleregister, View.ALPHA, 1f).setDuration(100)
        val namatxt = ObjectAnimator.ofFloat(binding.namatxtregister, View.ALPHA, 1f).setDuration(100)
        val nama = ObjectAnimator.ofFloat(binding.NamaRegister, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.EmailBlokRegister, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.EmailBlokRegister, View.ALPHA, 1f).setDuration(100)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val txtlogin = ObjectAnimator.ofFloat(binding.Logintxt, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(email, password)
        }
        AnimatorSet().apply {
            playSequentially(title, namatxt, nama,btnlogin, txtlogin, emailtxt, passwordtxt, together)
            start()
        }
    }

    private fun RegisterUser() {
        val nama = binding.NamaRegister.text.toString()
        val email = binding.EmailRegister.text.toString()
        val password = binding.passwordRegister.text.toString()

        // Basic input validation (optional, depends on your needs)
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Log.e(ContentValues.TAG, "Registration failed: Please fill in all fields.")
            return
        }

        APIConfig.postRegister().register(nama, email, password).enqueue(object :
            Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            SharedPrefManager.getInstance(applicationContext).saveRegister(responseBody.user!!)
                        } else {
                            // Handle API-specific error (check for error code or message in response)
                            Log.e(ContentValues.TAG, "Registration failed: ${responseBody.error}")
                        }
                    } else {
                        // Handle unexpected empty response
                        Log.e(ContentValues.TAG, "Unexpected empty response from server")
                    }
                } else {
                    // Handle unsuccessful response (check for HTTP status code)
                    Log.e(ContentValues.TAG, "Registration failed (code: ${response.code()})")
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e(ContentValues.TAG, "Registration failed: ${t.message}")
            }
        })
    }
}