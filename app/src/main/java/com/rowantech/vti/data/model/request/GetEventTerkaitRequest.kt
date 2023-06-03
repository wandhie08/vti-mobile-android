package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetEventTerkaitRequest(

	@field:SerializedName("company_id")
	var companyId: String? = null,

	@field:SerializedName("size")
	var size: Int? = null,

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("page")
	var page: Int? = null
)
