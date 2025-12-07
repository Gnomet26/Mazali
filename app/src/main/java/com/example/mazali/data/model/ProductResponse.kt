package com.example.mazali.data.model

import com.example.mazali.data.FoodItem

data class ProductResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<FoodItem>
)