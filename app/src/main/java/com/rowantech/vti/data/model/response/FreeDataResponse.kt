package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class FreeDataResponse(
	val freeDataResponse: List<FreeDataResponseItem?>? = null
)

data class FreeDataResponseItem(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
