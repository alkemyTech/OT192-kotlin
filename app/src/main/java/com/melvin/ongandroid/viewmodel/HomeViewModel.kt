package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.*
import com.melvin.ongandroid.model.*
import javax.inject.Inject
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
    

    /**
     * slide list of MutableLiveData type
     * created on 24 April 2022 by Leonel Gomez
     */
    private val _slideList = MutableLiveData<List<Slide>>()
    val slideList: LiveData<List<Slide>> = _slideList

    /**
     * isLoading is a MediatorLiveData that contains three boolean MutableLiveData
     * they indicate if one of the three fetching is still in loading state
     * For each search, there is a MutableLiveData to indicate if the search is loading
     * created on 25 April 2022 by Leonel Gomez
     */
    var isLoading = MediatorLiveData<Boolean>()
    private var isSlideLoading = MutableLiveData(false)
    private var isNewsLoading = MutableLiveData(false)
    private var isTestimonialsLoading = MutableLiveData(false)

    init {
        //Set loading state of all search
        isSlideLoading.postValue(true)
        isNewsLoading.postValue(true)
        isTestimonialsLoading.postValue(true)

        //Configure sources of loading mediator
        initLoadingMediatorConfigurator()

        getTestimonials()
        fetchLatestNews()
        //get a list of slides on ViewModel init
        fetchSlides()
    }

    /**
     * Init loading mediator configurator
     * Configure isLoading: a LiveData subclass which may observe other LiveData objects and
     * will react on OnChanged events from them.
     * created on 25 April 2022 by Leonel Gomez
     */
    private fun initLoadingMediatorConfigurator() {
        with(isLoading) {
            addSource(isSlideLoading) {
                isLoading.value =
                    it || isNewsLoading.value ?: false || isTestimonialsLoading.value ?: false
            }
            addSource(isNewsLoading) {
                isLoading.value =
                    it || isSlideLoading.value ?: false || isTestimonialsLoading.value ?: false
            }
            addSource(isTestimonialsLoading) {
                isLoading.value =
                    it || isNewsLoading.value ?: false || isSlideLoading.value ?: false
            }
        }
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
                    //Reset loading state
                    isTestimonialsLoading.postValue(false)
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
                    //Reset loading state
                    isNewsLoading.postValue(false)
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
                    //Reset loading state
                    isSlideLoading.postValue(false)

                } catch (e: Exception) {
                    _slideList.postValue(listOf())
                }
            }
        }
    }
}