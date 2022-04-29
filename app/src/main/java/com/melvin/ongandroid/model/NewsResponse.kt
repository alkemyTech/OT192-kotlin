package com.melvin.ongandroid.model


import com.google.gson.annotations.SerializedName

data class NewsResponse(
    var success: Boolean = false,
    @SerializedName("data")
    var novedades: List<News> = listOf(),
    var message: String = ""
)