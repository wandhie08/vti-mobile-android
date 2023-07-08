package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateTemplateRequest(

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("templates")
	var templates: List<TemplatesItem?>? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null
)

data class TemplatesItem(

	@field:SerializedName("answer")
	var answer: String? = null,

	@field:SerializedName("template_id")
	var templateId: String? = null
)
