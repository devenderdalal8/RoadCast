package com.example.roadcast.presentation.di

import com.example.roadcast.data.dataSource.DogRemoteDataSource
import com.example.roadcast.data.repository.DogRepositoryImpl
import com.example.roadcast.domain.repository.DogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        dogRemoteDataSource: DogRemoteDataSource,
    ): DogRepository {
        return DogRepositoryImpl(dogRemoteDataSource)
    }
}