package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.NewsResponse
import com.melvin.ongandroid.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    val testimonials = MutableLiveData<NewsResponse<MutableList<HomeTestimonials>>>()

    var repository = Repository()

        fun onCreate() {
        viewModelScope.launch {
            val result = repository.getTestimonials()

            if (!result.data.isNullOrEmpty()) {
                testimonials.postValue(result)
            }
        }
    }
}