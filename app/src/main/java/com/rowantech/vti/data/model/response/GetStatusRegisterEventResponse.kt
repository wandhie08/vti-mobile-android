package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetStatusRegisterEventResponse(

	@field:SerializedName("winners")
	val winners: String? = null,

	@field:SerializedName("form_registration")
	val formRegistration: Any? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("registration_status")
	val registrationStatus: String? = null,

	@field:SerializedName("registration_id")
	val registrationId: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("name_customer")
	val nameCustomer: String? = null,

	@field:SerializedName("shippment")
	val shippment: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("submission")
	val submission: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("transaction")
	val transaction: String? = null,

	@field:SerializedName("free_data")
	val freeData: String? = null
)
