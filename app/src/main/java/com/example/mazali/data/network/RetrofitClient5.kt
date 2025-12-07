package com.example.mazali.data.network

import com.example.mazali.Consts
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient5 {

    private val BASE_URL = Consts().BASE_URL+"/api/v1/bots/"  // Android emulator uchun

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}