package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetDateEventResponse(

	@field:SerializedName("evtP")
	val evtP: String? = null,

	@field:SerializedName("val2P")
	val val2P: String? = null,

	@field:SerializedName("winP")
	val winP: String? = null,

	@field:SerializedName("subP")
	val subP: String? = null,

	@field:SerializedName("regP")
	val regP: String? = null,

	@field:SerializedName("val1P")
	val val1P: String? = null
)
