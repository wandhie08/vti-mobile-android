package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class BugsReportRequest(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
