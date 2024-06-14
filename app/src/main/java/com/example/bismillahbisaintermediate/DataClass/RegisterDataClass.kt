package com.example.bismillahbisaintermediate.DataClass

data class RegisterDataClass(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
