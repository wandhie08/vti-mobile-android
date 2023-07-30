package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetPricingResponse(

	@field:SerializedName("data")
	val data: List<DataCourier?>? = null,

	@field:SerializedName("errors")
	val errors: List<Any?>? = null
)

data class DataCourier(

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("estimation")
	val estimation: String? = null,

	@field:SerializedName("insuranceValue")
	val insuranceValue: Int? = null,

	@field:SerializedName("supportCod")
	val supportCod: Boolean? = null,

	@field:SerializedName("courier")
	val courier: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("service")
	val service: String? = null,

	@field:SerializedName("courierCode")
	val courierCode: String? = null,

	@field:SerializedName("returnRate")
	val returnRate: Double? = null,

	@field:SerializedName("returnLevel")
	val returnLevel: String? = null
)
