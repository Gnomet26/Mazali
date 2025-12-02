package com.example.mazali.data.network

import com.example.mazali.data.model.LoginRequest
import com.example.mazali.data.model.LoginResponse
import com.example.mazali.data.model.RegisterRequest
import com.example.mazali.data.model.RegisterResponse
import com.example.mazali.data.model.UpdateRequest
import com.example.mazali.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Header

interface AuthApi {

    @POST("/api/auth/")
    suspend fun register(
        @Query("action") action: String = "register",
        @Body body: RegisterRequest
    ): Response<RegisterResponse>

    @POST("/api/auth/")
    suspend fun login(
        @Query("action") action: String = "login",
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @POST("/api/auth/")
    suspend fun profile(
        @Query("action") action: String = "profile",
        @Header("Authorization") token: String
    ): Response<RegisterResponse> // yoki ProfileResponse

    @POST("/api/auth/")
    suspend fun logout(
        @Query("action") action: String = "logout",
        @Header("Authorization") token: String
    ): Response<Unit>
    @POST("/api/auth/")
    suspend fun updateUser(
        @Query("action") action: String = "update",
        @Body body: UpdateRequest,
        @Header("Authorization") token: String
    ): Response<UserDto>
}
