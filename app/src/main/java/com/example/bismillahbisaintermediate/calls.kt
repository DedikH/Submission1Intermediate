package com.example.bismillahbisaintermediate

sealed class calls<out R> private constructor() {
    data class Success<out T>(val data: T) : calls<T>()
    data class Error(val error: String) : calls<Nothing>()
    object Loading : calls<Nothing>()
}