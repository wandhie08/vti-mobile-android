package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class RegisterEventRequest(

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null
)
