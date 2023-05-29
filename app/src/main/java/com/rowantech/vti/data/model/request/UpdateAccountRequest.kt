package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateAccountRequest(

	@field:SerializedName("phone_number")
	var phoneNumber: String? = null,

	@field:SerializedName("avatar")
	var avatar: String? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null,

	@field:SerializedName("name_customer")
	var nameCustomer: String? = null,

	@field:SerializedName("email")
	var email: String? = null
)
