package com.example.mazali.data

data class NotificationItem(
    val id: Int,
    val user: Int,
    val title: String,
    val message: String,
    val type: String,
    var is_read: Boolean,
    val created_at: String
)
