package com.example.roadcast.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcast.data.model.ApiResponse
import com.example.roadcast.data.util.Resource
import com.example.roadcast.data.util.Utility
import com.example.roadcast.domain.useCase.GetDogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val dogUseCase: GetDogUseCase
) : ViewModel() {
    private val dogList = MutableLiveData<Resource<ApiResponse>>()
    val dogLiveData: LiveData<Resource<ApiResponse>> = dogList

    fun getDogImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dogList.postValue(Resource.Loading())
                if (Utility.isNetworkAvailable(context)) {
                    val apiResult = dogUseCase.execute()
                    dogList.postValue(apiResult)
                } else {
                    dogList.postValue(Resource.Error("Internet is not available"))
                }
            } catch (ex: Exception) {
                dogList.postValue(Resource.Error(ex.message ?: "Error"))
            }
        }
    }
}