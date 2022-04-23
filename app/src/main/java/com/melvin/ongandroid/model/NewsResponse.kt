package com.melvin.ongandroid.model

import com.google.gson.annotations.SerializedName

// data class that represents the response from the Api
data class NewsResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T,
    @SerializedName("message") val message: String
)
