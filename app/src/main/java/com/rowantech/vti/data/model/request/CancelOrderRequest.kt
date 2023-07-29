package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CancelOrderRequest(

	@field:SerializedName("order_id")
    var orderId: String? = null
)
