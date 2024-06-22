package com.example.bismillahbisaintermediate.View.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.SharedPrefManager
import com.example.bismillahbisaintermediate.View.Login.Login
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


        playAnimation()
        btnRegister()
        LoginIntent()
        CallError()
    }

    private fun CallError(){
        binding.EmailBlokRegister.emailerror(binding.EmailRegister)
        binding.passwordBlokRegister.passworderror(binding.passwordRegister)
    }

    private fun btnRegister(){
        val button = binding.btnRegister
        button.setOnClickListener{
            RegisterUser()
        }
    }

    private fun LoginIntent(){
        val button = binding.Logintxt
        button.setOnClickListener{
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
    }



    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.fotoregister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleregister, View.ALPHA, 1f).setDuration(100)
        val nama = ObjectAnimator.ofFloat(binding.NamaRegister, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.EmailBlokRegister, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.EmailBlokRegister, View.ALPHA, 1f).setDuration(100)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val txtlogin = ObjectAnimator.ofFloat(binding.Logintxt, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(email, password)
        }
        AnimatorSet().apply {
            playSequentially(title, nama, btnlogin, txtlogin, together)
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
                    Toast.makeText(this@Register, "User Berhasil Dibuat", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Register, Login::class.java)
                    startActivity(intent)
                    finish()
                    if (responseBody != null && responseBody.user != null) {
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