package com.example.bismillahbisaintermediate.DataClass

data class LoginDataClass(
    val email: String,
    val token: String,
    val password: String,
    val isLogin: Boolean = false
)
