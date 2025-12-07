package com.example.mazali.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazali.data.model.BuyurtmaRequest
import com.example.mazali.data.model.BuyurtmaResponse
import com.example.mazali.data.model.Order
import com.example.mazali.data.repository.BuyurtmaRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class BuyurtmaViewModel(private val repository: BuyurtmaRepository) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _createOrderResponse = MutableLiveData<BuyurtmaResponse>()
    val createOrderResponse: LiveData<BuyurtmaResponse> get() = _createOrderResponse

    private val _cancelOrderResponse = MutableLiveData<Map<String, String>>()
    val cancelOrderResponse: LiveData<Map<String, String>> get() = _cancelOrderResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Buyurtmalar ro'yxatini olish
    fun fetchOrders(token: String) {
        viewModelScope.launch {
            try {
                val ordersList: List<Order> = repository.getOrders(token)
                _orders.postValue(ordersList)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Unknown error")
            }
        }
    }

    // Yangi buyurtma yaratish
    fun createOrder(token: String, request: BuyurtmaRequest) {
        viewModelScope.launch {
            try {
                val response = repository.createOrder(token, request)

                if (response.isSuccessful) {
                    _createOrderResponse.postValue(response.body())
                    fetchOrders(token)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.postValue("Error ${response.code()}: $errorBody")
                }

            } catch (e: Exception) {
                _error.postValue(e.message ?: "Unknown error")
            }
        }
    }

    // Buyurtmani bekor qilish
    fun cancelOrder(token: String, orderId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Map<String, String>> = repository.cancelOrder(token, orderId)
                if (response.isSuccessful) {
                    _cancelOrderResponse.postValue(response.body())
                    fetchOrders(token) // bekor qilingach ro'yxatni yangilash
                } else {
                    _error.postValue("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Unknown error")
            }
        }
    }
}