package com.example.getfluent

import com.example.frontend_robots.networking.APIService
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkModule {

    private const val BASE_URL = "http://localhost:5000/"

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    fun buildRetrofitClient(): APIService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(APIService::class.java)
    }
}