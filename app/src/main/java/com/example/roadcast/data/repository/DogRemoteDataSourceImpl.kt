package com.example.roadcast.data.repository

import com.example.roadcast.data.api.RestApi
import com.example.roadcast.data.dataSource.DogRemoteDataSource
import com.example.roadcast.data.model.ApiResponse
import retrofit2.Response

class DogRemoteDataSourceImpl(
    private val restApi: RestApi
) : DogRemoteDataSource {
    override suspend fun getRandomImage(): Response<ApiResponse> {
        return restApi.getNewsHeadLines()
    }
}