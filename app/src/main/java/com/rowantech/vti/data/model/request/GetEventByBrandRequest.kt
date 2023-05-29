package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetEventByBrandRequest(

	@field:SerializedName("size")
	var size: Int? = null,

	@field:SerializedName("page")
	var page: Int? = null,

	@field:SerializedName("type_event")
	var typeEvent: String? = null,

	@field:SerializedName("brand_id")
	var brandId: String? = null
)
