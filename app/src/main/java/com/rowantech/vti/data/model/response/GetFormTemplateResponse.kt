package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetFormTemplateResponse(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("templates")
	val templates: List<TemplatesItem?>? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null
)

data class TemplatesItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("question_type")
	val questionType: String? = null,

	@field:SerializedName("free_data1")
	val freeData1: String? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("optional")
	val optional: String? = null,

	@field:SerializedName("required")
	val required: String? = null,

	@field:SerializedName("template_name")
	val templateName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("template_id")
	val templateId: String? = null,

	@field:SerializedName("template_type")
	val templateType: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("validation")
	val validation: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
