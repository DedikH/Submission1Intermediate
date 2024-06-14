package com.example.submission1intermediate.API.Response

import com.example.bismillahbisaintermediate.DataClass.RegisterDataClass
import com.google.gson.annotations.SerializedName

data class ResponseRegister(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user : RegisterDataClass
)
