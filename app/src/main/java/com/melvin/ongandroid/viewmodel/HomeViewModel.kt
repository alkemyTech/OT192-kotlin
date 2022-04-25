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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    private val _testimonials = MutableLiveData<GenericResponse<MutableList<HomeTestimonials>>>()
    val testimonials: LiveData<GenericResponse<MutableList<HomeTestimonials>>> = _testimonials
    
    private val _newsResponse: MutableLiveData<NewsResponse> = MutableLiveData(NewsResponse())
    val newsResponse: LiveData<NewsResponse> = _newsResponse

    // LiveData to catch the error from the getTestimonials() and control the UI
    private val _errorTestimonials: MutableLiveData<String> = MutableLiveData()
    val errorTestimonials: LiveData<String> = _errorTestimonials
    
    init {
        getTestimonials()
        fetchLatestNews()
    }

    /*
    * Function that gets the testimonials and post it in the _testimonials MutableLiveData
    * In case the query fails, it will post the error in the _errorTestimonials MutableLiveData
    */
    fun getTestimonials() {
        viewModelScope.launch(IO) {
            repo.getTestimonials()
                .catch { throwable -> _errorTestimonials.postValue(throwable.message)  }
                .collect{ testimonialsResponse ->
                    _testimonials.postValue(testimonialsResponse)
                    _errorTestimonials.postValue("")
            }
        }        
    }

    /**
     * Function that fetch latestNews and post it in a mutable LiveData.
     * Uses Repository function with flow. When collected, posted values on[_newsResponse].
     */
    private fun fetchLatestNews(){
        viewModelScope.launch(IO) {
            repo.fetchLatestNews()
                    /*deje este catch porque se me crashea la app y no puedo comprobar
                    * la captura de error de testimonials*/
                .catch { throwable -> println("ESTE ES EL ERROR"+throwable.message) }
                .collect{ newsResponse ->
                    _newsResponse.postValue(newsResponse)
            }
        }
    }
}