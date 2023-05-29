package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetEventByTypeResponse(

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

	@field:SerializedName("events")
	val events: List<EventsItem?>? = null
)


data class EventsItem(

	@field:SerializedName("quota_condition")
	val quotaCondition: String? = null,

	@field:SerializedName("prize_description")
	val prizeDescription: String? = null,

	@field:SerializedName("free_data3")
	val freeData3: String? = null,

	@field:SerializedName("free_data2")
	val freeData2: String? = null,

	@field:SerializedName("free_data1")
	val freeData1: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("form_validation")
	val formValidation: String? = null,

	@field:SerializedName("submission_online_form_header")
	val submissionOnlineFormHeader: String? = null,

	@field:SerializedName("event_type")
	val eventType: String? = null,

	@field:SerializedName("online_form")
	val onlineForm: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("first_time")
	val firstTime: String? = null,

	@field:SerializedName("prize_category")
	val prizeCategory: String? = null,

	@field:SerializedName("tag_id")
	val tagId: String? = null,

	@field:SerializedName("bank_account")
	val bankAccount: String? = null,

	@field:SerializedName("realtime")
	val realtime: String? = null,

	@field:SerializedName("subscriber")
	val subscriber: Int? = null,

	@field:SerializedName("date_condition")
	val dateCondition: String? = null,

	@field:SerializedName("check_in")
	val checkIn: String? = null,

	@field:SerializedName("npwp")
	val npwp: String? = null,

	@field:SerializedName("select_product")
	val selectProduct: String? = null,

	@field:SerializedName("allow_multiple")
	val allowMultiple: String? = null,

	@field:SerializedName("winning_prize")
	val winningPrize: String? = null,

	@field:SerializedName("brand_name")
	val brandName: String? = null,

	@field:SerializedName("banners")
	val banners: List<BannersItem?>? = null,

	@field:SerializedName("brand_id")
	val brandId: String? = null,

	@field:SerializedName("last_periode")
	val lastPeriode: String? = null,

	@field:SerializedName("first_periode")
	val firstPeriode: String? = null,

	@field:SerializedName("registration_date")
	val registrationDate: String? = null,

	@field:SerializedName("submission_online_select_form")
	val submissionOnlineSelectForm: String? = null,

	@field:SerializedName("courier")
	val courier: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("tagline")
	val tagline: String? = null,

	@field:SerializedName("assigned")
	val assigned: String? = null,

	@field:SerializedName("youtube_url")
	val youtubeUrl: String? = null,

	@field:SerializedName("location_offline")
	val locationOffline: String? = null,

	@field:SerializedName("header_registration")
	val headerRegistration: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("short_description")
	val shortDescription: String? = null,

	@field:SerializedName("closing_date")
	val closingDate: String? = null,

	@field:SerializedName("periode_date")
	val periodeDate: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("location_check_in")
	val locationCheckIn: String? = null,

	@field:SerializedName("announcement_date")
	val announcementDate: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("participant_quota")
	val participantQuota: Int? = null,

	@field:SerializedName("last_time")
	val lastTime: String? = null,

	@field:SerializedName("item_condition")
	val itemCondition: String? = null,

	@field:SerializedName("qr_code")
	val qrCode: String? = null,

	@field:SerializedName("total_brands")
	val totalBrands: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("form_registration")
	val formRegistration: String? = null,

	@field:SerializedName("location_online")
	val locationOnline: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("shipping_location")
	val shippingLocation: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("product_type")
	val productType: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("submission")
	val submission: String? = null,

	@field:SerializedName("select_form_registration")
	val selectFormRegistration: String? = null,

	@field:SerializedName("offline_shipment")
	val offlineShipment: String? = null,

	@field:SerializedName("type_date")
	val typeDate: String? = null,

	@field:SerializedName("building_street_number")
	val buildingStreetNumber: String? = null,

	@field:SerializedName("permanent_link")
	val permanentLink: String? = null
)

data class BannersItem(

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("banner_id")
	val bannerId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("banner")
	val banner: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)