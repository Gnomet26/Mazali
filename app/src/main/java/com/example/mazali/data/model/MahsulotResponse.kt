package com.example.mazali.data.model

data class MahsulotResponse(
    val id: Int,
    val name: String,
    val category: String,
    val price: String,
    val comment: String?,
    val image: String,
    val created_at: String
)