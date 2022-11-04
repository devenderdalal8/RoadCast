package com.example.roadcast.domain.repository

import com.example.roadcast.data.model.ApiResponse
import com.example.roadcast.data.util.Resource

interface DogRepository {
    suspend fun getRandomImage(): Resource<ApiResponse>
}