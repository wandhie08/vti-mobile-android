package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetParameterResponse(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null,

	@field:SerializedName("parameters")
	val parameters: List<ParametersItem?>? = null
)

data class ParametersItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("parameter_code")
	val parameterCode: String? = null,

	@field:SerializedName("free_data2")
	val freeData2: String? = null,

	@field:SerializedName("parameter_icon")
	val parameterIcon: String? = null,

	@field:SerializedName("free_data1")
	val freeData1: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("parameter_name")
	val parameterName: String? = null,

	@field:SerializedName("parameter_type")
	val parameterType: String? = null,

	@field:SerializedName("parameter_id")
	val parameterId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
