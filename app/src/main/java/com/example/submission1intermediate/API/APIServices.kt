package com.example.submission1intermediate.API

import com.example.submission1intermediate.API.Response.LoginResponse
import com.example.submission1intermediate.API.Response.ResponseRegister
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIServices {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("name") name: String,
        @Field("email") email: String,
    ): Call<LoginResponse>
}