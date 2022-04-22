package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.NewsResponse
import retrofit2.http.GET

interface OngApiService {

    @GET("news")
    suspend fun fetchLatestNews() : NewsResponse
}