package com.example.mazali.data.network

import com.example.mazali.data.NotificationItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService2 {
    // GET /notification/ — barcha notifications ro'yxatini olish
    @GET("notification/")
    abstract fun getNotifications(): Call<List<NotificationItem>>

    // POST /notification/<NOTIF_ID>/read/ — notificationni o'qilgan deb belgilash
    @POST("notification/{id}/read/")
    fun markNotificationAsRead(
        @Path("id") notificationId: Int
    ): Call<NotificationItem>
}