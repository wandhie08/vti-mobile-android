package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class CreateCommentResponse(

	@field:SerializedName("is_admin")
	val isAdmin: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("discussion_id")
	val discussionId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("comment_id")
	val commentId: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("likes")
	val likes: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
