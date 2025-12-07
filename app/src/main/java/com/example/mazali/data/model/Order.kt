package com.example.mazali.data.model

data class Order(
    val id: Int,
    val status: String?,
    val createdAt: String?,
    val lat: Double?,
    val long: Double?,
    val products: List<ProductItem>, // items ichidagi product va quantity dan yaratiladi
    var isExpanded: Boolean = false
)