package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetPostalCodeRequest(

	@field:SerializedName("name")
	var name: String? = null
)
