package com.example.frontend_robots.networking

import com.example.frontend_robots.domain.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("search")
    fun search(
        @Query(IMAGE) image: String? = null,
    ) : Call<SearchResponse>

    companion object {
        private const val IMAGE = "image"

    }
}

