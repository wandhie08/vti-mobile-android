package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetTransactionProductEventResponse(

	@field:SerializedName("total_item")
	var totalItem: Int? = null,

	@field:SerializedName("product_event")
	var productEvent: List<ProductEventItem?>? = null,

	@field:SerializedName("total")
	var total: Int? = null
)

data class ProductEventItem(

	@field:SerializedName("total_product")
	var totalProduct: Int? = null,

	@field:SerializedName("price")
	var price: Int? = null,

	@field:SerializedName("product_id")
	var productId: String? = null,

	@field:SerializedName("sub_total")
	var subTotal: Int? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("photo")
	var photo: String? = null,

	var stock: Int? = 0,

	@field:SerializedName("title")
	var title: String? = null
)
