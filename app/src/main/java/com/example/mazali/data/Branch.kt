package com.example.mazali.data

data class Branch(
    val id: String,
    val region: String,      // viloyat nomi (primary)
    val address: String      // shahar, ko'cha, uy № (secondary)
)
