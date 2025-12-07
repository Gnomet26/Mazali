package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mazali.data.model.ProductResponse
import com.example.mazali.data.repository.ProductRepository
import androidx.lifecycle.viewModelScope
import com.example.mazali.data.FoodItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<FoodItem>>()
    val products: LiveData<List<FoodItem>> get() = _products

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var currentPage = 1
    private var isLoading = false

    // ----------------------------
    // Avvalgi funksiya: barcha mahsulotlar
    // ----------------------------
    fun fetchProducts(page: Int? = null, category: String? = null, search: String? = null) {
        currentPage = 1
        repository.getProducts(page, category, search).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    _products.value = response.body()?.results ?: emptyList()
                } else {
                    _error.value = "Server xatosi: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _error.value = "Tarmoq xatosi: ${t.message}"
            }
        })
    }

    // ----------------------------
    // Avvalgi funksiya: next page
    // ----------------------------
    fun fetchNextPage(category: String? = null, search: String? = null) {
        if (isLoading) return
        isLoading = true
        currentPage += 1

        repository.getNextPage(currentPage, category, search).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val newProducts = response.body()?.results ?: emptyList()
                    val currentList = _products.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(newProducts)
                    _products.value = currentList
                } else {
                    _error.value = "Server xatosi: ${response.code()}"
                    currentPage =1
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _error.value = "Tarmoq xatosi: ${t.message}"
                currentPage = 1
                isLoading = false
            }
        })
    }

    // ----------------------------
    // Kategoriya bo‘yicha birinchi page
    // ----------------------------
    fun fetchCategoryProducts(category: String) {
        if (isLoading) return
        isLoading = true
        currentPage = 1

        repository.fetchCategory(category).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    _products.value = response.body()?.results ?: emptyList()
                } else {
                    _error.value = "Server xatosi: ${response.code()}"
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _error.value = "Tarmoq xatosi: ${t.message}"
                isLoading = false
            }
        })
    }

    // ----------------------------
    // Kategoriya bo‘yicha next page
    // ----------------------------
    fun fetchCategoryNextPage(category: String) {
        if (isLoading) return
        isLoading = true
        currentPage += 1

        repository.fetchCategoryNextPage(currentPage, category).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val newProducts = response.body()?.results ?: emptyList()
                    val currentList = _products.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(newProducts)
                    _products.value = currentList
                } else {
                    _error.value = "Server xatosi: ${response.code()}"
                    currentPage = 1
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _error.value = "Tarmoq xatosi: ${t.message}"
                currentPage = 1
                isLoading = false
            }
        })
    }

    // ------------------------------------------------------
    // 🔍 Yangi funksiya: SEARCH bo‘yicha birinchi page
    // ------------------------------------------------------
    fun fetchSearchProducts(query: String) {
        if (isLoading) return
        isLoading = true
        currentPage = 1

        repository.fetchSearch(query).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    _products.value = response.body()?.results ?: emptyList()
                } else {
                    _error.value = "Server xatosi: ${response.code()}"
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _error.value = "Tarmoq xatosi: ${t.message}"
                isLoading = false
            }
        })
    }

    // ------------------------------------------------------
    // 🔍 Yangi funksiya: SEARCH bo‘yicha keyingi page
    // ------------------------------------------------------
    fun fetchSearchNextPage(query: String) {
        if (isLoading) return
        isLoading = true
        currentPage += 1

        repository.fetchSearchNextPage(currentPage, query).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val newProducts = response.body()?.results ?: emptyList()
                    val currentList = _products.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(newProducts)
                    _products.value = currentList
                } else {
                    _error.value = "Server xatosi: ${response.code()}"
                    currentPage = 1
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                _error.value = "Tarmoq xatosi: ${t.message}"
                currentPage = 1
                isLoading = false
            }
        })
    }
}