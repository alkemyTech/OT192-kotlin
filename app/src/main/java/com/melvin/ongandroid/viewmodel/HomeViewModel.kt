package com.melvin.ongandroid.viewmodel

import androidx.lifecycle.*
import com.melvin.ongandroid.model.*
import javax.inject.Inject
import com.melvin.ongandroid.repository.OngRepository
import com.melvin.ongandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: OngRepository) : ViewModel() {

    private val _testimonials = MutableLiveData<GenericResponse<MutableList<HomeTestimonials>>>()
    val testimonials: LiveData<GenericResponse<MutableList<HomeTestimonials>>> = _testimonials


    private val _newsState: MutableLiveData<Resource<NewsResponse>> =
        MutableLiveData(Resource.loading())
    val newsState: LiveData<Resource<NewsResponse>> = _newsState



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

    /**
     * slide error of MutableLiveData String type
     * to catch the error of the function of getting Slide
     * created on 25 April 2022 by Leonel Gomez
     */
    private val _slideError: MutableLiveData<String> = MutableLiveData()
    val slideError: LiveData<String> = _slideError
  

    /* LiveData that handles Massive Failure*/
    private val _massiveFailure: MutableLiveData<Boolean> = MutableLiveData(false)
    val massiveFailure: LiveData<Boolean> = _massiveFailure

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
        checkMassiveFailure()
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
     * updated on 25 April 2022 by Leonel Gomez
     */
    fun fetchSlides() {
        //coroutine to get the listing asynchronously
        viewModelScope.launch(IO) {
            repo.getSlides()
                .catch { throwable -> _slideError.postValue(throwable.message) }
                .collect { response ->
                    try {
                        if (response.success)
                            _slideList.postValue(response.data!!)
                        else
                            _slideList.postValue(listOf())
                        _slideError.postValue("")
                    } catch (e: Exception) {
                        _slideList.postValue(listOf())
                        _slideError.postValue(e.message)
                    }
                    //Reset loading state
                    isSlideLoading.postValue(false)
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