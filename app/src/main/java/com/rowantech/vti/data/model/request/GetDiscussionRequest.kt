package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetDiscussionRequest(

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("size")
	var size: Int? = null,

	@field:SerializedName("page")
	var page: Int? = null
)
