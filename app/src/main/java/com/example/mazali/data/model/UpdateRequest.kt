package com.example.mazali.data.model

import com.google.gson.annotations.SerializedName

class UpdateRequest(
    val name: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val password: String,
)