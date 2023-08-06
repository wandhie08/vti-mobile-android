package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetTotalDateEventResponse(

	@field:SerializedName("subD")
	val subD: Int? = null,

	@field:SerializedName("regD")
	val regD: Int? = null,

	@field:SerializedName("val1D")
	val val1D: Int? = null,

	@field:SerializedName("totD")
	val totD: Int? = null,

	@field:SerializedName("evtD")
	val evtD: Int? = null,

	@field:SerializedName("val2D")
	val val2D: Int? = null,

	@field:SerializedName("winD")
	val winD: Int? = null
)
