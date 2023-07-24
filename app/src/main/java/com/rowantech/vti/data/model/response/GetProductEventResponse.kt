package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetProductEventResponse(

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

	@field:SerializedName("products")
	val products: List<ProductsItem?>? = null
)

data class ProductsItem(

	@field:SerializedName("max_quantity")
	val maxQuantity: String? = null,

	@field:SerializedName("volume_length")
	val volumeLength: Int? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("free_shipping")
	val freeShipping: String? = null,

	@field:SerializedName("initial_stock")
	val initialStock: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("optional")
	val optional: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("brand_id")
	val brandId: String? = null,

	@field:SerializedName("max_stock")
	val maxStock: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("shipping_method")
	val shippingMethod: String? = null,

	@field:SerializedName("volume_height")
	val volumeHeight: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("name_template")
	val nameTemplate: String? = null,

	@field:SerializedName("min_stock")
	val minStock: Int? = null,

	@field:SerializedName("free_giveaway")
	val freeGiveaway: String? = null,

	@field:SerializedName("stock")
	var stock: Int? = null,

	@field:SerializedName("volume_width")
	val volumeWidth: Int? = null,

	@field:SerializedName("status")
	val status: String? = null


)
