package com.melvin.ongandroid.repository

import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.services.OngApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class OngRepository @Inject constructor(private val apiService: OngApiService) {

    suspend fun fetchLatestNews(): Flow<NewsResponse> = flow {
        emit(apiService.fetchLatestNews())
    }
}