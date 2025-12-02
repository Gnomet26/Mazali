package com.example.mazali.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,
    val name: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("device_token") val deviceToken: String?
)