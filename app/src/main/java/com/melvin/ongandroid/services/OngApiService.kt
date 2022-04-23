package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.NewsResponse
import retrofit2.http.GET

interface OngApiService {

    // Get testimonials from the API
    @GET("testimonials")
    suspend fun getTestimonials(): GenericResponse<MutableList<HomeTestimonials>>
  
    @GET("news")
    suspend fun fetchLatestNews() : NewsResponse

}