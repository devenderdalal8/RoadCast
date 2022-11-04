package com.example.roadcast.data.api

import com.example.roadcast.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET


interface RestApi {
    @GET("random")
    suspend fun getNewsHeadLines(): Response<ApiResponse>
}