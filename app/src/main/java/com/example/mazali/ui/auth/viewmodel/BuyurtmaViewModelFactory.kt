package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mazali.data.repository.BuyurtmaRepository

class BuyurtmaViewModelFactory(
    private val repository: BuyurtmaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BuyurtmaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BuyurtmaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}