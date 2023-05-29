package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetBrandsTerkaitRequest(

	@field:SerializedName("company_id")
	var companyId: String? = null,

	@field:SerializedName("size")
	var size: Int? = null,

	@field:SerializedName("page")
	var page: Int? = null
)
