package com.example.mazali.data.model

data class ItemResponse(
    val id: Int,
    val product: MahsulotResponse,
    val quantity: Int,
    val comment: String?
)