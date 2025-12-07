package com.example.mazali.data.repository

import com.example.mazali.data.NotificationItem
import com.example.mazali.data.network.ApiService2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationRepository(private val apiService: ApiService2) {

    // GET notifications
    fun getNotifications(
        onSuccess: (List<NotificationItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.getNotifications().enqueue(object : Callback<List<NotificationItem>> {
            override fun onResponse(
                call: Call<List<NotificationItem>>,
                response: Response<List<NotificationItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Server xatosi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<NotificationItem>>, t: Throwable) {
                onError("Tarmoqqa ulanishda xatolik: ${t.message}")
            }
        })
    }

    // POST mark as read
    fun markAsRead(
        notificationId: Int,
        onSuccess: (NotificationItem) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.markNotificationAsRead(notificationId).enqueue(object : Callback<NotificationItem> {
            override fun onResponse(call: Call<NotificationItem>, response: Response<NotificationItem>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Server xatosi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NotificationItem>, t: Throwable) {
                onError("Tarmoqqa ulanishda xatolik: ${t.message}")
            }
        })
    }
}
