package com.rowantech.vti.utilities

import android.content.Context
import android.net.ConnectivityManager
import com.rowantech.vti.data.model.response.MessageResponse
import com.google.gson.Gson

object NetworkUtil {

    var errorResponse: MessageResponse? = null
    fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }
    fun errorConnection(message :String): String {
        errorResponse= MessageResponse()
        errorResponse!!.error = "505"
        errorResponse!!.success = false
        return Gson().toJson(errorResponse!!)
    }
}