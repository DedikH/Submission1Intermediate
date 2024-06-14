package com.example.bismillahbisaintermediate.View.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailerror()
        passworderror()
        playAnimation()
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
}