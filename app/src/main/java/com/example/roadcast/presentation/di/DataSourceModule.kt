package com.example.roadcast.presentation.di

import com.example.roadcast.data.api.RestApi
import com.example.roadcast.data.dataSource.DogRemoteDataSource
import com.example.roadcast.data.repository.DogRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun provideDogRemoteDataSource(restApi: RestApi): DogRemoteDataSource {
        return DogRemoteDataSourceImpl(restApi)
    }
}