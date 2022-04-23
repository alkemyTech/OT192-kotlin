package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

interface OngApiService {

    // Get testimonials from the API
    @GET("api/testimonials")
    suspend fun getTestimonials(): Response<NewsResponse<MutableList<HomeTestimonials>>>
}