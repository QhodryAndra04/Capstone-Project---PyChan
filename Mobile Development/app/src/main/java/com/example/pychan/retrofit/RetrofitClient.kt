package com.example.pychan.retrofit

import com.example.pychan.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://cc-backend-294905168660.asia-southeast2.run.app/"
    private const val ML_URL = "https://ml-backend-294905168660.asia-southeast2.run.app/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mlRetrofit = Retrofit.Builder()
        .baseUrl(ML_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
    val mlApiService: ApiService = mlRetrofit.create(ApiService::class.java)
}