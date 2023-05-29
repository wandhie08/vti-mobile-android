package com.rowantech.vti.data.db

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
data class Authorization (
        val id: Int? = null,
        var accessToken: String? = null
)