package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetDiscussionResponse(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("discussions")
	val discussions: List<DiscussionsItem?>? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null
)

data class CommentDiscussItem(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("comment_id")
	val commentId: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("name_customer")
	val nameCustomer: String? = null,

	@field:SerializedName("is_admin")
	val isAdmin: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("discussion_id")
	val discussionId: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("likes")
	val likes: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DiscussionsItem(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("name_customer")
	val nameCustomer: String? = null,

	@field:SerializedName("is_read")
	val isRead: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("discussion_id")
	val discussionId: String? = null,

	@field:SerializedName("commentDiscuss")
	val commentDiscuss: List<CommentDiscussItem?>? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null
)
