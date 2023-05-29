package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateCommentRequest(

	@field:SerializedName("is_admin")
	val isAdmin: String? = null,

	@field:SerializedName("discussion_id")
	val discussionId: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)
