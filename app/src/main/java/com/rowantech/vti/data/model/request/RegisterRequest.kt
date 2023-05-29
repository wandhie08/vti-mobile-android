package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

	@field:SerializedName("address")
	var address: String? = null,

	@field:SerializedName("gender")
	var gender: String? = null,

	@field:SerializedName("city")
	var city: String? = null,

	@field:SerializedName("avatar")
	var avatar: String? = null,

	@field:SerializedName("name_customer")
	var nameCustomer: String? = null,

	@field:SerializedName("nik")
	var nik: String? = null,

	@field:SerializedName("password")
	var password: String? = null,

	@field:SerializedName("province")
	var province: String? = null,

	@field:SerializedName("district")
	var district: String? = null,

	@field:SerializedName("phone_number")
	var phoneNumber: String? = null,

	@field:SerializedName("identity_type")
	var identityType: String? = null,

	@field:SerializedName("postal_code")
	var postalCode: String? = null,

	@field:SerializedName("email")
	var email: String? = null,

	@field:SerializedName("username")
	var username: String? = null
)
