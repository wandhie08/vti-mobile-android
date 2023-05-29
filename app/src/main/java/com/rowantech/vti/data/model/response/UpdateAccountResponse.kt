package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class UpdateAccountResponse(

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("name_customer")
	val nameCustomer: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
