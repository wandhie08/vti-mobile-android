package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetPostalCodeResponse(

	@field:SerializedName("data")
	var data: List<DataItem?>? = null,

	@field:SerializedName("success")
	var success: Boolean? = null,

	@field:SerializedName("errors")
	var errors: List<Any?>? = null
)

data class DataItem(

	@field:SerializedName("village_code")
	var villageCode: String? = null,

	@field:SerializedName("city_name")
	var cityName: String? = null,

	@field:SerializedName("subdistrict_code")
	var subdistrictCode: String? = null,

	@field:SerializedName("city_code")
	var cityCode: String? = null,

	@field:SerializedName("_id")
	var id: String? = null,

	@field:SerializedName("village_name")
	var villageName: String? = null,

	@field:SerializedName("province_code")
	var provinceCode: String? = null,

	@field:SerializedName("postal_code")
	var postalCode: String? = null,

	@field:SerializedName("subdistrict_name")
	var subdistrictName: String? = null,

	@field:SerializedName("province_name")
	var provinceName: String? = null,

	@field:SerializedName("updatedAt")
	var updatedAt: Any? = null
)
