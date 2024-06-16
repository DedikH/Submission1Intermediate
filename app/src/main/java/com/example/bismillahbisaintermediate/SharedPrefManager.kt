package com.example.bismillahbisaintermediate

import android.content.Context
import com.example.bismillahbisaintermediate.DataClass.RegisterDataClass

class SharedPrefManager private constructor(private val mCtx: Context) {

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

    private val SHARED_PREF_NAME = "my_shared_preff"
    fun saveRegister(user: RegisterDataClass) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("name", user.name)
        editor.putString("email", user.email)
        editor.putString("password", user.password)
        editor.apply()
    }
}