package com.example.mazali.data.model

data class RegisterResponse(
    val user: UserDto,
    val token: String
)