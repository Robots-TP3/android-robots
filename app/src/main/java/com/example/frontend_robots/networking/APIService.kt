package com.example.frontend_robots.networking

import com.example.frontend_robots.domain.SearchResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIService {

    @Multipart
    @POST("search")
    fun search(
        @Part image: MultipartBody.Part
    ) : Call<SearchResponse>

    companion object {
        private const val IMAGE = "image"
    }
}

