package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazali.data.model.ChatResponse
import com.example.mazali.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _reply = MutableLiveData<ChatResponse>()
    val reply: LiveData<ChatResponse> get() = _reply

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.sendMessage(message)
                _reply.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
