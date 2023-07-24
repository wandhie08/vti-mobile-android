package com.rowantech.vti.utilities

import java.util.*

object Constant {
    val DATE_FORMAT = "dd MM yyyy HH:mm:ss"
    //val BASE_URL = "http://10.0.2.2/api/v1/"
    val BASE_URL = "http://156.67.219.199/api/v1/"

    val LOGIN ="customer/login"
    val REGISTER ="customer/create"
    val UPDATE_ADDRESS ="customer/updateAddress"
    val UPDATE_PASSWORD ="customer/updatePassword"
    val BUGS_REPORT ="bugs/create"
    val UPDATE_ACCOUNT ="customer/update"
    val LIST_DISCUSSION ="discussion/byEvent"
    val CREATE_COMMENT ="comments/create"
    val CREATE_DISCUSSION ="discussion/create"
    val CREATE_TEMPLATE ="participant/createTemplate"
    val GET_STATUS_TEMPLATE ="participant/statusTemplate"

    val EVENT_TERKAIT ="dashboard/eventListByCompanyMB"
    val EVENT_BYBRAND ="event/eventListByBrandMB"
    val CUSTOMER_BYID ="customer/byCustomer"
    val FORM_TEMPLATE =" participant/formTemplateEvent"

    val UNSUBSCRIBE_BRAND ="subscriber/delete"
    val STATUS_SUBSCRIBE ="subscriber/byUser"
    val SUBSCRIBE_BRAND ="subscriber/create"

    val STATUS_EVENT ="participant/byStatus"
    val SUBSCRIBE_EVENT ="participant/create"

    val BRAND_TERKAIT ="brand/mb/byCompany"
    val BRAND="brand"
    val EVENT_BYTYPE ="event/byTypeMB"
    val PRODUCT="product/byEvent"
    val UNIQUE_CODE="parameter/unique-code"

    val MESSAGE_CEK_KONEKSI = "Please check your connection"

    val DB_NAME = "SICANTIKDB"



}