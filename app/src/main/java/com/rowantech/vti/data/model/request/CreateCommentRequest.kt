package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateCommentRequest(

	@field:SerializedName("is_admin")
	var isAdmin: String? = null,

	@field:SerializedName("discussion_id")
	var discussionId: String? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null,

	@field:SerializedName("title")
	var title: String? = null,

	@field:SerializedName("message")
	var message: String? = null,

	@field:SerializedName("type")
	var type: String? = null
)
