package com.example.mazali.data.repository

import com.example.mazali.data.model.ChatRequest
import com.example.mazali.data.model.ChatResponse
import com.example.mazali.data.network.ChatApiService


class ChatRepository(private val api: ChatApiService) {

    suspend fun sendMessage(message: String): ChatResponse {
        return api.sendMessage(ChatRequest(message))
    }
}
