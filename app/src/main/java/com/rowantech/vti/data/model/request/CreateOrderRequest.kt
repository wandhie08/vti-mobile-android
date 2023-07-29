package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(

	@field:SerializedName("destination_subdistrict_code")
	var destinationSubdistrictCode: String? = null,

	@field:SerializedName("destination_postal_code")
	var destinationPostalCode: String? = null,

	@field:SerializedName("origin_contact_name")
	var originContactName: String? = null,

	@field:SerializedName("destination_contact_phone")
	var destinationContactPhone: String? = null,

	@field:SerializedName("order_number")
	var orderNumber: String? = null,

	@field:SerializedName("delivery_time")
	var deliveryTime: String? = null,

	@field:SerializedName("is_cod")
	var isCod: Boolean? = null,

	@field:SerializedName("origin_subdistrict_name")
	var originSubdistrictName: String? = null,

	@field:SerializedName("origin_city_name")
	var originCityName: String? = null,

	@field:SerializedName("destination_note")
	var destinationNote: String? = null,

	@field:SerializedName("delivery_type")
	var deliveryType: String? = null,

	@field:SerializedName("destination_contact_address")
	var destinationContactAddress: String? = null,

	@field:SerializedName("origin_contact_phone")
	var originContactPhone: String? = null,

	@field:SerializedName("origin_postal_code")
	var originPostalCode: String? = null,

	@field:SerializedName("destination_lat")
	var destinationLat: Any? = null,

	@field:SerializedName("destination_long")
	var destinationLong: Any? = null,

	@field:SerializedName("origin_province_name")
	var originProvinceName: String? = null,

	@field:SerializedName("destination_city_code")
	var destinationCityCode: String? = null,

	@field:SerializedName("origin_city_code")
	var originCityCode: String? = null,

	@field:SerializedName("destination_contact_name")
	var destinationContactName: String? = null,

	@field:SerializedName("destination_province_name")
	var destinationProvinceName: String? = null,

	@field:SerializedName("origin_long")
	var originLong: Any? = null,

	@field:SerializedName("delivery_note")
	var deliveryNote: String? = null,

	@field:SerializedName("origin_lat")
	var originLat: Any? = null,

	@field:SerializedName("origin_province_code")
	var originProvinceCode: String? = null,

	@field:SerializedName("origin_contact_email")
	var originContactEmail: String? = null,

	@field:SerializedName("origin_note")
	var originNote: String? = null,

	@field:SerializedName("is_send_company")
	var isSendCompany: Boolean? = null,

	@field:SerializedName("destination_contact_email")
	var destinationContactEmail: String? = null,

	@field:SerializedName("courier")
	var courier: String? = null,

	@field:SerializedName("courier_service")
	var courierService: String? = null,

	@field:SerializedName("destination_province_code")
	var destinationProvinceCode: String? = null,

	@field:SerializedName("origin_subdistrict_code")
	var originSubdistrictCode: String? = null,

	@field:SerializedName("origin_contact_address")
	var originContactAddress: String? = null,

	@field:SerializedName("destination_subdistrict_name")
	var destinationSubdistrictName: String? = null,

	@field:SerializedName("items")
	var items: List<ItemOrder?>? = null,

	@field:SerializedName("transaction")
	var transaction: Transaction? = null,

	@field:SerializedName("destination_city_name")
	var destinationCityName: String? = null
)

data class ItemOrder(

	@field:SerializedName("total_value")
	var totalValue: Int? = null,

	@field:SerializedName("weight_uom")
	var weightUom: String? = null,

	@field:SerializedName("product_id")
	var productId: String? = null,

	@field:SerializedName("qty")
	var qty: Int? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("width")
	var width: Int? = null,

	@field:SerializedName("length")
	var length: Int? = null,

	@field:SerializedName("description")
	var description: String? = null,

	@field:SerializedName("weight")
	var weight: Int? = null,

	@field:SerializedName("value")
	var value: Int? = null,

	@field:SerializedName("dimension_uom")
	var dimensionUom: String? = null,

	@field:SerializedName("height")
	var height: Int? = null
)

data class Transaction(

	@field:SerializedName("fee_insurance")
	var feeInsurance: Int? = null,

	@field:SerializedName("total_value")
	var totalValue: Int? = null,

	@field:SerializedName("total_cod")
	var totalCod: Int? = null,

	@field:SerializedName("coolie")
	var coolie: Int? = null,

	@field:SerializedName("package_category")
	var packageCategory: String? = null,

	@field:SerializedName("length")
	var length: Int? = null,

	@field:SerializedName("discount")
	var discount: Int? = null,

	@field:SerializedName("weight")
	var weight: Int? = null,

	@field:SerializedName("package_content")
	var packageContent: String? = null,

	@field:SerializedName("method_payment")
	var methodPayment: String? = null,

	@field:SerializedName("event_id")
	var eventId: String? = null,

	@field:SerializedName("shipping_charge")
	var shippingCharge: Int? = null,

	@field:SerializedName("subtotal")
	var subtotal: Int? = null,

	@field:SerializedName("width")
	var width: Int? = null,

	@field:SerializedName("customer_id")
	var customerId: String? = null,

	@field:SerializedName("is_insuranced")
	var isInsuranced: Boolean? = null,

	@field:SerializedName("unique_code")
	var uniqueCode: Int? = null,

	@field:SerializedName("height")
	var height: Int? = null
)
