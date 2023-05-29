package com.rowantech.vti.api

import com.rowantech.vti.data.model.request.LoginRequest
import retrofit2.Call
import retrofit2.http.*

interface VTIService {
    @POST("customer/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<String>

    @POST("auth/logout")
    fun logout(
        @Header("Authorization") authorization: String
    ): Call<String>

    @GET
    fun param2(
            @Header("Authorization") accessToken: String,
            @Url() url: String
    ): Call<String>

    @GET
    fun paramWithId(
            @Header("Authorization") accessToken: String,
            @Url() url: String
    ): Call<String>


    @POST
    fun paramWIthBody(
        @Header("Authorization") authorization: String,
        @Url() url: String,
        @Body request: String
    ): Call<String>

    @POST
    fun paramPOST(
            @Header("Authorization") accessToken: String,
            @Url() url: String
    ): Call<String>


}