package com.example.mazali.data.model

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    val error: String? = null,
    val detail: String? = null,
    @SerializedName("phone_number") val phoneNumber: List<String>? = null
)