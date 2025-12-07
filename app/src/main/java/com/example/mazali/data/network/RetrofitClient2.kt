package com.example.mazali.network

import com.example.mazali.Consts
import com.example.mazali.data.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient2 {

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Consts().BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
