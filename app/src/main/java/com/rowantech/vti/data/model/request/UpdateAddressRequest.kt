package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class UpdateAddressRequest(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("phone_number")
	var phoneNumber: String? = null,

	@field:SerializedName("address")
	var address: String? = null,

	@field:SerializedName("province")
	var province: String? = null,

	@field:SerializedName("city")
	var city: String? = null,

	@field:SerializedName("district")
	var district: String? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null,

	@field:SerializedName("postal_code")
	var postalCode: String? = null
)
