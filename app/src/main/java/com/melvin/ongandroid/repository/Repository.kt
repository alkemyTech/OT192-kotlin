package com.melvin.ongandroid.repository

import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.services.OngApiManager

class Repository {

    private val api = OngApiManager()

    suspend fun getTestimonials(): NewsResponse<MutableList<HomeTestimonials>>{
        val response = api.getTestimonials()
        return response
    }
}