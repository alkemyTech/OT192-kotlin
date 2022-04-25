package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.model.Slide
import retrofit2.http.GET
import retrofit2.http.Query

interface OngApiService {

    // Get testimonials from the API
    @GET("testimonials")
    suspend fun getTestimonials(): GenericResponse<MutableList<HomeTestimonials>>
  
    @GET("news")
    suspend fun fetchLatestNews() : NewsResponse

    /**
     * Get slides
     * slides request to API
     * created on 24 April 2022 by Leonel Gomez
     *
     * @param search    String: Term of search
     * @param skip  Integer: Total entries to skip in result
     * @param limit Integer: Total entries to retrieve
     * @return a GenericResponse (success, data, message) where data is of type list of slides
     */
    @GET("slides")
    suspend fun getSlides(
        @Query("search") search: String? = null,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null,
    ): GenericResponse<List<Slide>>

}