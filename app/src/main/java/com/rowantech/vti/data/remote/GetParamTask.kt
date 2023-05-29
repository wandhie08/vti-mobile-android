package com.rowantech.vti.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rowantech.vti.api.*
import com.rowantech.vti.data.Resource
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.NetworkUtil
import java.io.IOException

class GetParamTask(
        private val accessToken: String,
        private val url: String,
        private val service: VTIService
) : Runnable {
    private val _liveData = MutableLiveData<Resource<String>>()
    val liveData: LiveData<Resource<String>> = _liveData

    override fun run() {

        try {
            val response = service.param2(accessToken, url).execute()
            val apiResponse = ApiResponse.create(response)
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    _liveData.postValue(Resource.success(response.body()))
                }
                is ApiEmptyResponse -> {
                    Resource.success(false)
                }
                is ApiErrorResponse -> {
                    Resource.error(apiResponse.errorMessage, true)
                    _liveData.postValue(Resource.error(apiResponse.errorMessage, apiResponse.errorMessage))
                }
            }

        } catch (e: IOException) {
            Resource.error(NetworkUtil.errorConnection(Constant.MESSAGE_CEK_KONEKSI), true)
            _liveData.postValue(
                    Resource.error(
                            NetworkUtil.errorConnection(Constant.MESSAGE_CEK_KONEKSI), NetworkUtil.errorConnection(
                            Constant.MESSAGE_CEK_KONEKSI)))
        }

    }
}