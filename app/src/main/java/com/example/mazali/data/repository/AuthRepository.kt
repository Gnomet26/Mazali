package com.example.mazali.data.repository

import android.content.Context
import com.example.mazali.data.model.*
import com.example.mazali.data.network.AuthApi
import com.example.mazali.utils.PrefsManager
import retrofit2.HttpException
import java.io.IOException


class AuthRepository(private val api: AuthApi) {

    // ------------------ REGISTER ------------------
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = api.register(body = request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseError(response.code(), response.message())))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.message()}"))
        }
    }

    // ------------------ LOGIN ------------------
    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(body = request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseError(response.code(), response.message())))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.message()}"))
        }
    }

    suspend fun updateUser(token: String, updateReq: UpdateRequest): Result<UserDto> {
        return try {
            val response = api.updateUser(body = updateReq, token = "Token $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(parseError(response.code(), response.message())))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("Server error: ${e.message()}"))
        }
    }


    // ------------------ LOGOUT ------------------
    suspend fun logout(token: String): Result<Unit> {
        return try {
            val response = api.logout(token = "Token $token")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(parseError(response.code(), response.message())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Logout error: ${e.message}"))
        }
    }

    private fun parseError(code: Int, message: String): String {
        return "Error $code: $message"
    }
}