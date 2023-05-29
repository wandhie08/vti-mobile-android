package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("customer")
	var customer: Customer? = null,

	@field:SerializedName("token")
	var token: String? = null
)

