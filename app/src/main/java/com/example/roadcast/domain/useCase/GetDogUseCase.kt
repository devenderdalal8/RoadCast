package com.example.roadcast.domain.useCase

import com.example.roadcast.data.model.ApiResponse
import com.example.roadcast.data.util.Resource
import com.example.roadcast.domain.repository.DogRepository
import javax.inject.Inject

class GetDogUseCase @Inject constructor(private val dogRepository: DogRepository) {
    suspend fun execute(): Resource<ApiResponse> {
        return dogRepository.getRandomImage()
    }
}