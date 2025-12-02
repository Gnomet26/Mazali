package com.example.mazali.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    val password: String,
    @SerializedName("device_token") val deviceToken: String
)