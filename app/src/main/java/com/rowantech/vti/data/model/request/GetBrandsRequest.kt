package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetBrandsRequest(

	@field:SerializedName("size")
	var size: Int? = null,

	@field:SerializedName("page")
	var page: Int? = null
)
