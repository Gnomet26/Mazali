package com.example.mazali.data.model

data class LoginResponse(
    val token: String,
    val user: UserDto,
)