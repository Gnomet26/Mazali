package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mazali.data.NotificationItem
import com.example.mazali.data.repository.NotificationRepository

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationItem>>()
    val notifications: LiveData<List<NotificationItem>> get() = _notifications

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // GET notifications
    fun fetchNotifications() {
        _loading.value = true
        repository.getNotifications(
            onSuccess = {
                _notifications.value = it
                _loading.value = false
            },
            onError = {
                _error.value = it
                _loading.value = false
            }
        )
    }

    // POST mark as read
    fun markNotificationAsRead(notificationId: Int) {
        repository.markAsRead(
            notificationId,
            onSuccess = { updatedNotification ->
                // Mahalliy listni yangilash
                val currentList = _notifications.value?.toMutableList() ?: mutableListOf()
                val index = currentList.indexOfFirst { it.id == updatedNotification.id }
                if (index != -1) {
                    currentList[index] = updatedNotification
                    _notifications.value = currentList
                }
            },
            onError = { _error.value = it }
        )
    }
}