package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(

	@field:SerializedName("success")
	var success: Boolean? = null,

	@field:SerializedName("error")
	var error: String? = null,

	@field:SerializedName("message")
	var message: String? = null,

	@field:SerializedName("status")
	var status: Int? = null,
)
