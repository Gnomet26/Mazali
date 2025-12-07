package com.example.mazali.data.network


import com.example.mazali.data.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/v1/product/products/")
    fun getProducts(
        @Query("page") page: Int? = null,            // Paginatsiya uchun
        @Query("category") category: String? = null, // Kategoriya filter
        @Query("search") search: String? = null      // Qidiruv
    ): Call<ProductResponse>

}