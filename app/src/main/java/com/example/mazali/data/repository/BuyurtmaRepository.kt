package com.example.mazali.data.repository

import com.example.mazali.Consts
import com.example.mazali.data.model.BuyurtmaRequest
import com.example.mazali.data.model.BuyurtmaResponse
import com.example.mazali.data.model.Order
import com.example.mazali.data.model.ProductItem
import com.example.mazali.data.network.RetrofitClient4
import retrofit2.Response

class BuyurtmaRepository {
    private val api = RetrofitClient4.instance

    // Buyurtmalar ro'yxatini olish
    suspend fun getOrders(token: String): List<Order> {
        val response = api.getOrders("Token $token")
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.map { orderResponse ->
                val products = orderResponse.items.map { item ->
                    ProductItem(
                        id = item.product.id,
                        name = item.product.name,
                        price = item.product.price,
                        count = item.quantity,
                        image = "${Consts().BASE_URL}${item.product.image}"
                    )
                }
                Order(
                    id = orderResponse.id,
                    status = orderResponse.status,
                    createdAt = orderResponse.created_at,
                    lat = orderResponse.lat.toDoubleOrNull(),
                    long = orderResponse.long.toDoubleOrNull(),
                    products = products
                )
            }
        } else {
            return emptyList()
        }
    }


    // Yangi buyurtma yaratish
    suspend fun createOrder(token: String, request: BuyurtmaRequest): Response<BuyurtmaResponse> {
        return api.createOrder("Token $token", request)
    }

    // Buyurtmani bekor qilish
    suspend fun cancelOrder(token: String, orderId: Int): Response<Map<String, String>> {
        return api.cancelOrder("Token $token", orderId)
    }
}