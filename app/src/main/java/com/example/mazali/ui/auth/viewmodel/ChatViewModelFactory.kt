package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mazali.data.repository.ChatRepository
import kotlin.jvm.java

class ChatViewModelFactory(private val repository: ChatRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(repository) as T
        }
        throw kotlin.IllegalArgumentException("Unknown ViewModel class")
    }
}
