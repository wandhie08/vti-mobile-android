package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetByCustomerRequest(

	@field:SerializedName("customer_id")
	var customerId: String? = null
)

