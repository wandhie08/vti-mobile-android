package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class SubscribeBrandRequest(

	@field:SerializedName("user_id")
	var userId: String? = null,

	@field:SerializedName("brand_id")
	var brandId: String? = null
)
