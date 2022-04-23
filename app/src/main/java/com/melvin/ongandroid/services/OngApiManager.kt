package com.melvin.ongandroid.services

import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OngApiManager {

    private val retrofit = RetrofitInstance.getRetrofit()

    suspend fun getTestimonials(): NewsResponse<MutableList<HomeTestimonials>> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(OngApiService::class.java).getTestimonials()
            response.body() ?: NewsResponse(false, mutableListOf(), "")

        }
    }
}