package com.example.roadcast.data.repository

import com.example.roadcast.data.dataSource.DogRemoteDataSource
import com.example.roadcast.data.model.ApiResponse
import com.example.roadcast.data.util.Resource
import com.example.roadcast.domain.repository.DogRepository
import retrofit2.Response

class DogRepositoryImpl(
    val dogRemoteDataSource: DogRemoteDataSource,
) : DogRepository {
    override suspend fun getRandomImage(): Resource<ApiResponse> {
        return responseToResource(dogRemoteDataSource.getRandomImage())
    }

    private fun responseToResource(response: Response<ApiResponse>): Resource<ApiResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}