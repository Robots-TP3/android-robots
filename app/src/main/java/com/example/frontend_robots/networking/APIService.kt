package com.example.frontend_robots.networking

import com.example.frontend_robots.domain.SearchData
import com.example.frontend_robots.domain.SearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {

    @POST("search")
    fun search(
        @Body image: SearchData
    ) : Call<SearchResponse>

    companion object {
        private const val IMAGE = "image"
    }
}

