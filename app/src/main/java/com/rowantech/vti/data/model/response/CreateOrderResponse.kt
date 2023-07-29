package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("errors")
	val errors: List<Any?>? = null
)

data class ShippingChargeCalculate(

	@field:SerializedName("discount_shipping_charge_amount")
	val discountShippingChargeAmount: Int? = null,

	@field:SerializedName("cod_fee_percentage")
	val codFeePercentage: Int? = null,

	@field:SerializedName("discount_shipping_charge_percentage")
	val discountShippingChargePercentage: Int? = null,

	@field:SerializedName("cod_fee_value")
	val codFeeValue: Int? = null,

	@field:SerializedName("total_amount")
	val totalAmount: Int? = null,

	@field:SerializedName("billing_type")
	val billingType: String? = null
)

data class Contact(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("isSendAsCompany")
	val isSendAsCompany: Boolean? = null
)

data class Data(

	@field:SerializedName("tenant_id")
	val tenantId: String? = null,

	@field:SerializedName("shipping_charge_calculate")
	val shippingChargeCalculate: ShippingChargeCalculate? = null,

	@field:SerializedName("charge")
	val charge: Int? = null,

	@field:SerializedName("order_number")
	val orderNumber: String? = null,

	@field:SerializedName("origin")
	val origin: Origin? = null,

	@field:SerializedName("destination")
	val destination: Destination? = null,

	@field:SerializedName("delivery_time")
	val deliveryTime: String? = null,

	@field:SerializedName("item_summary")
	val itemSummary: String? = null,

	@field:SerializedName("is_cod")
	val isCod: Boolean? = null,

	@field:SerializedName("delivery_note")
	val deliveryNote: String? = null,

	@field:SerializedName("qris_response")
	val qrisResponse: QrisResponse? = null,

	@field:SerializedName("tracking_info")
	val trackingInfo: TrackingInfo? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("platform")
	val platform: String? = null,

	@field:SerializedName("order_problem_counter")
	val orderProblemCounter: List<Any?>? = null,

	@field:SerializedName("courier")
	val courier: String? = null,

	@field:SerializedName("additional_info")
	val additionalInfo: AdditionalInfo? = null,

	@field:SerializedName("service")
	val service: String? = null,

	@field:SerializedName("delivery_type")
	val deliveryType: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null,

	@field:SerializedName("transaction")
	val transaction: Transaction? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Origin(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("city_name")
	val cityName: String? = null,

	@field:SerializedName("subdistrict_code")
	val subdistrictCode: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("contact")
	val contact: Contact? = null,

	@field:SerializedName("city_code")
	val cityCode: String? = null,

	@field:SerializedName("province_code")
	val provinceCode: String? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("subdistrict_name")
	val subdistrictName: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("province_name")
	val provinceName: String? = null
)

data class AdditionalInfo(

	@field:SerializedName("courier")
	val courier: Courier? = null
)

data class ItemsItem(

	@field:SerializedName("total_value")
	val totalValue: Int? = null,

	@field:SerializedName("weight_uom")
	val weightUom: String? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("length")
	val length: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("value")
	val value: Int? = null,

	@field:SerializedName("dimension_uom")
	val dimensionUom: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class Transaction(

	@field:SerializedName("fee_insurance")
	val feeInsurance: Int? = null,

	@field:SerializedName("total_value")
	val totalValue: Int? = null,

	@field:SerializedName("total_cod")
	val totalCod: Int? = null,

	@field:SerializedName("coolie")
	val coolie: Int? = null,

	@field:SerializedName("package_category")
	val packageCategory: String? = null,

	@field:SerializedName("length")
	val length: Int? = null,

	@field:SerializedName("discount")
	val discount: Int? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("package_content")
	val packageContent: String? = null,

	@field:SerializedName("shipping_charge")
	val shippingCharge: Int? = null,

	@field:SerializedName("subtotal")
	val subtotal: Int? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("is_insuranced")
	val isInsuranced: Boolean? = null,

	@field:SerializedName("height")
	val height: Int? = null
)

data class TrackingInfo(

	@field:SerializedName("booking_code")
	val bookingCode: String? = null,

	@field:SerializedName("airwaybill")
	val airwaybill: String? = null,

	@field:SerializedName("etd")
	val etd: String? = null,

	@field:SerializedName("destination_code")
	val destinationCode: String? = null
)

data class Destination(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("city_name")
	val cityName: String? = null,

	@field:SerializedName("subdistrict_code")
	val subdistrictCode: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("contact")
	val contact: Contact? = null,

	@field:SerializedName("city_code")
	val cityCode: String? = null,

	@field:SerializedName("province_code")
	val provinceCode: String? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("subdistrict_name")
	val subdistrictName: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("province_name")
	val provinceName: String? = null
)

data class Courier(

	@field:SerializedName("pickup_merchant_name")
	val pickupMerchantName: String? = null,

	@field:SerializedName("pickup_merchant_code")
	val pickupMerchantCode: String? = null,

	@field:SerializedName("items")
	val items: List<Any?>? = null
)

data class QrisResponse(

	@field:SerializedName("refid")
	val refid: String? = null,

	@field:SerializedName("rawqr")
	val rawqr: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
