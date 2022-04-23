package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melvin.ongandroid.model.HomeTestimonials
import com.melvin.ongandroid.model.GenericResponse
import com.melvin.ongandroid.model.NewsResponse
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.melvin.ongandroid.repository.OngRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    private val _testimonials = MutableLiveData<GenericResponse<MutableList<HomeTestimonials>>>()
    val testimonials: LiveData<GenericResponse<MutableList<HomeTestimonials>>> = _testimonials
    
    private val _newsResponse: MutableLiveData<NewsResponse> = MutableLiveData(NewsResponse())
    val newsResponse: LiveData<NewsResponse> = _newsResponse
    
    init {
        getTestimonials()
        fetchLatestNews()
    }

    // Function that gets the testimonials and post it in the _testimonials MutableLiveData
    private fun getTestimonials() {
        viewModelScope.launch(IO) {
            repo.getTestimonials().collect{ testimonialsResponse ->
                _testimonials.postValue(testimonialsResponse)
            }
        }        
    }

    /**
     * Function that fetch latestNews and post it in a mutable LiveData.
     * Uses Repository function with flow. When collected, posted values on[_newsResponse].
     */
    private fun fetchLatestNews(){
        viewModelScope.launch(IO) {
            repo.fetchLatestNews().collect{ newsResponse ->
                _newsResponse.postValue(newsResponse)
            }
        }
    }
}