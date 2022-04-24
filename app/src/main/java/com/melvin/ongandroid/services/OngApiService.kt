package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.Contact
import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.NewsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface OngApiService {

    // Get testimonials from the API
    @GET("testimonials")
    suspend fun getTestimonials(): GenericResponse<MutableList<HomeTestimonials>>
  
    @GET("news")
    suspend fun fetchLatestNews() : NewsResponse

    // Post a new contact to the API
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("contacts")
    suspend fun sendContact(@Body contact: Contact): GenericResponse<List<Contact>>

}