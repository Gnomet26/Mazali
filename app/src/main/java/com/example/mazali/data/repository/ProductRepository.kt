package com.example.mazali.data.repository

import retrofit2.Call
import com.example.mazali.data.network.ApiService
import com.example.mazali.data.model.ProductResponse

class ProductRepository(private val apiService: ApiService) {

    // Avvalgi funksiya: barcha mahsulotlar
    fun getProducts(
        page: Int? = null,
        category: String? = null,
        search: String? = null
    ): Call<ProductResponse> {
        return apiService.getProducts(page, category, search)
    }

    // Avvalgi funksiya: keyingi page ma'lumotlari
    fun getNextPage(page: Int, category: String? = null, search: String? = null): Call<ProductResponse> {
        return apiService.getProducts(page, category, search)
    }

    // Kategoriya bo‘yicha birinchi page
    fun fetchCategory(category: String): Call<ProductResponse> {
        return apiService.getProducts(page = 1, category = category)
    }

    // Kategoriya bo‘yicha keyingi page
    fun fetchCategoryNextPage(page: Int, category: String): Call<ProductResponse> {
        return apiService.getProducts(page = page, category = category)
    }

    // 🔍 **Yangi funksiya: Search bo‘yicha birinchi page**
    fun fetchSearch(query: String): Call<ProductResponse> {
        return apiService.getProducts(page = 1, search = query)
    }

    // 🔍 **Yangi funksiya: Search bo‘yicha keyingi page**
    fun fetchSearchNextPage(page: Int, query: String): Call<ProductResponse> {
        return apiService.getProducts(page = page, search = query)
    }
}