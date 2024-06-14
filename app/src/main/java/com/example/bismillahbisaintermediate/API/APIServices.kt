package com.example.submission1intermediate.API

import com.example.bismillahbisaintermediate.Response.ListStoryResponse
import com.example.bismillahbisaintermediate.Response.LoginResponse
import com.example.submission1intermediate.API.Response.ResponseRegister
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStoriesAll(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Call<ListStoryResponse>
}