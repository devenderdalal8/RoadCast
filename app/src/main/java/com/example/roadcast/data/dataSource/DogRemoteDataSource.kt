package com.example.roadcast.data.dataSource

import com.example.roadcast.data.model.ApiResponse
import retrofit2.Response

interface DogRemoteDataSource {
    suspend fun getRandomImage(): Response<ApiResponse>
}