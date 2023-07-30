package com.rowantech.vti.data.model.request

import com.google.gson.annotations.SerializedName

data class GetPricingRequest(

	@field:SerializedName("destination_subdistrict_code")
	var destinationSubdistrictCode: String? = null,

	@field:SerializedName("destination_postal_code")
	var destinationPostalCode: Int? = null,

	@field:SerializedName("origin_city_code")
	var originCityCode: String? = null,

	@field:SerializedName("destination_province_name")
	var destinationProvinceName: String? = null,

	@field:SerializedName("origin_long")
	var originLong: String? = null,

	@field:SerializedName("is_cod")
	var isCod: Boolean? = null,

	@field:SerializedName("origin_lat")
	var originLat: String? = null,

	@field:SerializedName("origin_province_code")
	var originProvinceCode: String? = null,

	@field:SerializedName("origin_subdistrict_name")
	var originSubdistrictName: String? = null,

	@field:SerializedName("origin_city_name")
	var originCityName: String? = null,

	@field:SerializedName("destination_province_code")
	var destinationProvinceCode: String? = null,

	@field:SerializedName("origin_subdistrict_code")
	var originSubdistrictCode: String? = null,

	@field:SerializedName("origin_postal_code")
	var originPostalCode: Int? = null,

	@field:SerializedName("destination_lat")
	var destinationLat: String? = null,

	@field:SerializedName("destination_long")
	var destinationLong: String? = null,

	@field:SerializedName("origin_province_name")
	var originProvinceName: String? = null,

	@field:SerializedName("destination_city_code")
	var destinationCityCode: String? = null,

	@field:SerializedName("destination_subdistrict_name")
	var destinationSubdistrictName: String? = null,

	@field:SerializedName("items")
	var items: List<ItemOrder?>? = null,

	@field:SerializedName("destination_city_name")
	var destinationCityName: String? = null
)

data class ItemsItem(

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
