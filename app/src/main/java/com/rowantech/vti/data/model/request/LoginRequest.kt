package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(

	@field:SerializedName("username")
	var userEmail: String? = null,

	@field:SerializedName("password")
    var password: String? = null
)
