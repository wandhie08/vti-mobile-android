package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetBrandsResponse(

	@field:SerializedName("brands")
	val brands: List<BrandsItem?>? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("has_more")
	val hasMore: Boolean? = null
)

data class BrandsItem(

	@field:SerializedName("subscriber")
	val subscriber: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("npwp")
	val npwp: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("brand_name")
	val brandName: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("shipping_location")
	val shippingLocation: String? = null,

	@field:SerializedName("brand_id")
	val brandId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("courier")
	val courier: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("bank_name")
	val bankName: String? = null,

	@field:SerializedName("tagline")
	val tagline: String? = null,

	@field:SerializedName("assigned")
	val assigned: String? = null,

	@field:SerializedName("total_brands")
	val totalBrands: Int? = null,

	@field:SerializedName("building_street_number")
	val buildingStreetNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("bank_account")
	val bankAccount: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
