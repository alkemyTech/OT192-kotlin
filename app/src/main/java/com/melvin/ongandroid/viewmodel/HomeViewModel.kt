package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.*
import com.melvin.ongandroid.model.*
import javax.inject.Inject
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    private val _testimonials = MutableLiveData<GenericResponse<MutableList<HomeTestimonials>>>()
    val testimonials: LiveData<GenericResponse<MutableList<HomeTestimonials>>> = _testimonials


    private val _newsState: MutableLiveData<Resource<NewsResponse>> =
        MutableLiveData(Resource.loading())
    val newsState: LiveData<Resource<NewsResponse>> = _newsState


    /**
     * slide list of MutableLiveData type
     * created on 24 April 2022 by Leonel Gomez
     */
    private val _slideList = MutableLiveData<List<Slide>>()
    val slideList: LiveData<List<Slide>> = _slideList

    /* LiveData that handles Massive Failure*/
    private val _massiveFailure: MutableLiveData<Boolean> = MutableLiveData(false)
    val massiveFailure: LiveData<Boolean> = _massiveFailure


    init {
        getTestimonials()
        fetchLatestNews()
        //get a list of slides on ViewModel init
        fetchSlides()
        checkMassiveFailure()
    }

    // Function that gets the testimonials and post it in the _testimonials MutableLiveData
    private fun getTestimonials() {
        viewModelScope.launch(IO) {
            repo.getTestimonials().collect { testimonialsResponse ->
                _testimonials.postValue(testimonialsResponse)
            }
        }
    }

    /**
     * Function that fetch latestNews and post it in a mutable LiveData.
     * Collects from Repository and handle diverse States of response.
     * When
     * [Resource.Success] -> Request From Api was Succesfull
     * [Resource.ErrorApi] -> Request from Api had Errors
     * [Resource.ErrorThrowable] -> Catch Exceptions
     */
    private fun fetchLatestNews() {
        viewModelScope.launch(IO) {
            repo.fetchLatestNews().collect { resource ->
                when (resource) {
                    is Resource.Success ->
                        _newsState.postValue(Resource.success(resource.data!!))

                    is Resource.ErrorApi ->
                        _newsState.postValue(Resource.errorApi(resource.errorMessage!!))

                    is Resource.ErrorThrowable ->
                        _newsState.postValue(Resource.errorThrowable(resource.errorThrowable!!))

                    is Resource.Loading ->
                        _newsState.postValue(Resource.loading())

                }
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

    /** Function that handle massive Error from API.
     * Checks if all elements are not empty or null.
     *
     */

    private fun checkMassiveFailure() {
        viewModelScope.launch(IO) {
            delay(5000)
            _massiveFailure.postValue(
                _newsState.value?.data?.success == false &&
                        _slideList.value?.isNotEmpty() == false &&
                        _testimonials.value?.success == false
            )
        }
    }

    /**
     * When Error in API Call, retry all request and checks again fi successful
     */

    fun retryApiCallsHome() {
        fetchLatestNews()
        fetchSlides()
        getTestimonials()
        checkMassiveFailure()
    }
}