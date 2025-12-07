package com.example.mazali.data.network

import com.example.mazali.data.model.BuyurtmaRequest
import com.example.mazali.data.model.BuyurtmaResponse
import com.example.mazali.data.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService4 {
    @GET("orders/order/")
    suspend fun getOrders(
        @Header("Authorization") token: String
    ): Response<List<OrderResponse>>

    @POST("orders/order/")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: BuyurtmaRequest
    ): Response<BuyurtmaResponse>

    @DELETE("orders/order/{id}/")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("id") orderId: Int
    ): Response<Map<String, String>>
}