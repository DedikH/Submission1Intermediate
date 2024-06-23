package com.example.bismillahbisaintermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Injection.Injection
import com.example.bismillahbisaintermediate.View.AddStory.ViewModellAddStory
import com.example.bismillahbisaintermediate.View.ListStory.ListStoryViewModel
import com.example.bismillahbisaintermediate.View.ListStory.withmaps.MapsViewModel
import com.example.bismillahbisaintermediate.View.Login.LoginViewModel

class ViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListStoryViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(ViewModellAddStory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModellAddStory(userRepository) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {

            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}