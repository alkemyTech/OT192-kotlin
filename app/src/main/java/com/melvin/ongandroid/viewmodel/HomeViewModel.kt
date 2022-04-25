package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.*
import com.melvin.ongandroid.model.*
import javax.inject.Inject
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

    /**
     * slide list of MutableLiveData type
     * created on 24 April 2022 by Leonel Gomez
     */
    private val _slideList = MutableLiveData<List<Slide>>()
    val slideList: LiveData<List<Slide>> = _slideList

    init {
        getTestimonials()
        fetchLatestNews()
        //get a list of slides on ViewModel init
        fetchSlides()
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

    /**
     * Fetch slides
     * from repository
     * created on 24 April 2022 by Leonel Gomez
     */
    private fun fetchSlides() {
        //coroutine to get the listing asynchronously
        viewModelScope.launch(IO) {
            repo.getSlides().collect { response ->
                try {
                    if (response.success)
                        _slideList.postValue(response.data!!)
                    else
                        _slideList.postValue(listOf())

                } catch (e: Exception) {
                    _slideList.postValue(listOf())
                }
            }
        }
    }
}