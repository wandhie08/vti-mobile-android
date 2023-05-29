package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class CreateDiscussionResponse(

	@field:SerializedName("is_read")
	val isRead: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("discussion_id")
	val discussionId: String? = null,

	@field:SerializedName("commentDiscuss")
	val commentDiscuss: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
