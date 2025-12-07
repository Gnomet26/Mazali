package com.example.mazali.data.network

import com.example.mazali.Consts
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient4 {
    private val BASE_URL = Consts().BASE_URL+"/api/v1/"

    val instance: ApiService4 by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService4::class.java)
    }
}