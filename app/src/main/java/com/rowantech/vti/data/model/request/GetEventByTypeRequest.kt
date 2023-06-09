package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetEventByTypeRequest(

	@field:SerializedName("size")
	var size: Int? = null,

	@field:SerializedName("page")
	var page: Int? = null,

	@field:SerializedName("type_event")
	var typeEvent: String? = null,

	@field:SerializedName("type_account")
    var typeAccount: String? = null,

	@field:SerializedName("type_location")
	var typeLocation: String? = null

)
