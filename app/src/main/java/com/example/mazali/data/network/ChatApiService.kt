package com.example.mazali.data.network

import com.example.mazali.data.model.ChatRequest
import com.example.mazali.data.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {

    @POST("bot/")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse
}
