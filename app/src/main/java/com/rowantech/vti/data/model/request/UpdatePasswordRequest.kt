package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(

	@field:SerializedName("old_password")
	var oldPassword: String? = null,

	@field:SerializedName("new_password")
	var newPassword: String? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null
)
