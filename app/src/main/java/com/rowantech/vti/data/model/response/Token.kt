package com.rowantech.vti.data.model.response

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class Token(
    @field:SerializedName("access_token")
    val accessToken: String? = null,

    @field:SerializedName("token_type")
    val tokenType: String? = null,

    @field:SerializedName("expires_in")
    val expiresIn: Int? = null
)
