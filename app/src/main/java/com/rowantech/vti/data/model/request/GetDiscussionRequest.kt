package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetDiscussionRequest(

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null
)
