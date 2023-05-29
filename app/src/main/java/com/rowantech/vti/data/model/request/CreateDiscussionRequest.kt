package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateDiscussionRequest(

	@field:SerializedName("is_read")
	val isRead: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
