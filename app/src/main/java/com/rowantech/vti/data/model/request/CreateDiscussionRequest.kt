package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateDiscussionRequest(

	@field:SerializedName("is_read")
	var isRead: String? = null,

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null,

	@field:SerializedName("title")
	var title: String? = null,

	@field:SerializedName("type")
	var type: String? = null,

	@field:SerializedName("content")
	var content: String? = null
)
