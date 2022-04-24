package com.melvin.ongandroid.repository


import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.model.Slide
import com.melvin.ongandroid.services.OngApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class OngRepository @Inject constructor(private val apiService: OngApiService) {

    // Function that emits a GenericResponse, containing a list of Testimonials
    suspend fun getTestimonials(): Flow<GenericResponse<MutableList<HomeTestimonials>>> = flow {
        emit(apiService.getTestimonials())
    }

    /*
    Function that emits a NewsResponse, containing list Of News
     */
    suspend fun fetchLatestNews(): Flow<NewsResponse> = flow {
        emit(apiService.fetchLatestNews())
    }

    /**
     * Get slides
     * Repository function that return the list of slides from the API
     * created on 24 April 2022 by Leonel Gomez
     *
     * @return the list of Slides emitted by upstream
     */
    suspend fun getSlides(): Flow<GenericResponse<List<Slide>>> = flow {
        //Collects the value emitted by the upstream
        emit(apiService.getSlides())
    }
}