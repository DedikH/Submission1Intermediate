package com.example.bismillahbisaintermediate.Response

import com.google.gson.annotations.SerializedName

data class ErrorResponses(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String
)