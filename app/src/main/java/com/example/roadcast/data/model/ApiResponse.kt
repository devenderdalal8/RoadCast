package com.example.roadcast.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") @Expose var status: String? = "",
    @SerializedName("message") @Expose var message: String? = ""
)